package com.traelo.delivery.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.enums.PaymentStatus;
import com.traelo.delivery.enums.PlanType;
import com.traelo.delivery.model.PaymentPlan;
import com.traelo.delivery.model.dto.ActivateSubscriptionDTO;
import com.traelo.delivery.model.dto.CreatePaymentPlanDTO;
import com.traelo.delivery.model.dto.PaymentPlanDTO;
import com.traelo.delivery.model.dto.UpdatePaymentPlanDTO;
import com.traelo.delivery.repository.PaymentPlanRepository;
import com.traelo.delivery.service.PaymentPlanService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentPlanServiceImpl implements PaymentPlanService {
	@Autowired
	private PaymentPlanRepository paymentPlanRepository;

	private static final String MERCADO_PAGO_URL = "https://www.mercadopago.com.mx/subscriptions/checkout/v2?preapproval_plan_id=ea7d51d0517a479295a988be36d478bd";

	private static final Integer DEFAULT_TRIAL_DAYS = 14;

	private static final Double DEFAULT_PLAN_AMOUNT = 369.0;

	@Override
	@Transactional
	public PaymentPlanDTO createPaymentPlan(CreatePaymentPlanDTO request) {
	    try {
	        log.info("Creando PaymentPlan para businessId: {}, userId: {}", request.getBusinessId(), request.getUserId());

	        Optional<PaymentPlan> existingPlan = paymentPlanRepository.findByBusinessId(request.getBusinessId());
	        
	        if (existingPlan.isPresent()) {
	            PaymentPlan plan = existingPlan.get();
	            
	            log.info("Ya existe un PaymentPlan para este negocio. ID: {}, Estado actual: {}", plan.getId(), plan.getStatus());
	            
	            // Si está cancelado, reactivarlo como TRIAL
	            if (plan.getStatus() == PaymentStatus.CANCELLED) {
	                log.info("Reactivating cancelled plan ID: {}", plan.getId());
	                
	                LocalDateTime now = LocalDateTime.now();
	                plan.setStatus(PaymentStatus.TRIAL);
	                plan.setTrialStart(now);
	                plan.setTrialEnd(now.plusDays(DEFAULT_TRIAL_DAYS));
	                plan.setAmountMxn(DEFAULT_PLAN_AMOUNT);
	                plan.setLastPaymentDate(null);
	                plan.setNextBillingDate(null);
	                plan.setPaymentMethod(null);
	                plan.setPaymentProofUrl(null);
	                plan.setNotes("Reactivated from cancelled state");
	                
	                PaymentPlan paymentPlan = paymentPlanRepository.save(plan);
	                
	                log.info("Plan reactivado exitosamente. ID: {}, Nuevo estado: {}", paymentPlan.getId(), paymentPlan.getStatus());
	                
	                return convertToDTO(paymentPlan);
	            }
	            
	            throw new RuntimeException("El negocio ya tiene una suscripción (Estado: " +  plan.getStatus() + "). No se puede crear otra.");
	        }

	        // Si no existe, crear uno nuevo
	        String externalReference = generateExternalReference(request.getBusinessId());
	        LocalDateTime now = LocalDateTime.now();
	        
	        PaymentPlan paymentPlan = PaymentPlan.builder()
	                .businessId(request.getBusinessId())
	                .userId(request.getUserId())
	                .planType(PlanType.BUSINESS_PLAN)
	                .status(PaymentStatus.TRIAL)
	                .externalReference(externalReference)
	                .amountMxn(DEFAULT_PLAN_AMOUNT)
	                .trialDays(DEFAULT_TRIAL_DAYS)
	                .trialStart(now)
	                .trialEnd(now.plusDays(DEFAULT_TRIAL_DAYS))
	                .build();

	        paymentPlan = paymentPlanRepository.save(paymentPlan);

	        log.info("PaymentPlan creado exitosamente. ID: {}, BusinessId: {}", paymentPlan.getId(), request.getBusinessId());

	        return convertToDTO(paymentPlan);
	    } catch (Exception e) {
	        log.error("Error creando PaymentPlan: {}", e.getMessage(), e);
	        throw new RuntimeException("Error al crear plan de pago: " + e.getMessage());
	    }
	}

	@Override
	@Transactional(readOnly = true)
	public PaymentPlanDTO getPaymentPlanByBusinessId(Long businessId) {
		log.info("Obteniendo PaymentPlan para businessId: {}", businessId);

		return paymentPlanRepository.findByBusinessId(businessId).map(this::convertToDTO).orElseGet(() -> {
			// If it does not exist, return DTO with status NO_SUBSCRIPTION
			PaymentPlanDTO dto = new PaymentPlanDTO();
			dto.setBusinessId(businessId);
			dto.setStatus("NO_SUBSCRIPTION");
			dto.setHasActiveSubscription(false);
			dto.setIsTrial(false);
			dto.setIsActive(false);
			dto.setCanActivate(true);
			dto.setMercadoPagoUrl(MERCADO_PAGO_URL);
			return dto;
		});
	}

	@Override
	@Transactional
	public PaymentPlanDTO activatePaymentPlan(Long id, ActivateSubscriptionDTO request) {
		log.info("Activando PaymentPlan con ID: {}", id);

		PaymentPlan paymentPlan = paymentPlanRepository.findById(id).orElseThrow(() -> new RuntimeException("PaymentPlan no encontrado con id: " + id));

		// Verify that it can be activated
		if (paymentPlan.getStatus() != PaymentStatus.TRIAL && paymentPlan.getStatus() != PaymentStatus.PENDING && paymentPlan.getStatus() != PaymentStatus.CANCELLED) {
			throw new RuntimeException("El plan no puede ser activado en su estado actual: " + paymentPlan.getStatus());
		}

		LocalDateTime now = LocalDateTime.now();
		paymentPlan.setStatus(PaymentStatus.ACTIVE);
		paymentPlan.setLastPaymentDate(now);
		paymentPlan.setNextBillingDate(now.plusMonths(1));

		if (request.getPaymentMethod() != null) {
			paymentPlan.setPaymentMethod(request.getPaymentMethod());
		}
		if (request.getPaymentProofUrl() != null) {
			paymentPlan.setPaymentProofUrl(request.getPaymentProofUrl());
		}
		if (request.getNotes() != null) {
			paymentPlan.setNotes(request.getNotes());
		}

		paymentPlan = paymentPlanRepository.save(paymentPlan);
		log.info("PaymentPlan activado exitosamente. ID: {}, Método: {}", id, request.getPaymentMethod());

		return convertToDTO(paymentPlan);
	}

	@Override
	@Transactional
	public PaymentPlanDTO cancelPaymentPlan(Long id) {
		log.info("Cancelando PaymentPlan con ID: {}", id);

		PaymentPlan paymentPlan = paymentPlanRepository.findById(id).orElseThrow(() -> new RuntimeException("PaymentPlan no encontrado con id: " + id));

		paymentPlan.setStatus(PaymentStatus.CANCELLED);

		paymentPlan = paymentPlanRepository.save(paymentPlan);
		log.info("PaymentPlan cancelado exitosamente. ID: {}", id);

		return convertToDTO(paymentPlan);
	}

	@Override
	@Transactional
	public PaymentPlanDTO updatePaymentPlan(Long id, UpdatePaymentPlanDTO request) {
		log.info("Actualizando PaymentPlan con ID: {}", id);

		PaymentPlan paymentPlan = paymentPlanRepository.findById(id).orElseThrow(() -> new RuntimeException("PaymentPlan no encontrado con id: " + id));

		if (request.getStatus() != null) {
			paymentPlan.setStatus(PaymentStatus.valueOf(request.getStatus()));
		}
		if (request.getLastPaymentDate() != null) {
			paymentPlan.setLastPaymentDate(request.getLastPaymentDate());
		}
		if (request.getPaymentMethod() != null) {
			paymentPlan.setPaymentMethod(request.getPaymentMethod());
		}
		if (request.getPaymentProofUrl() != null) {
			paymentPlan.setPaymentProofUrl(request.getPaymentProofUrl());
		}
		if (request.getNotes() != null) {
			paymentPlan.setNotes(request.getNotes());
		}

		paymentPlan = paymentPlanRepository.save(paymentPlan);
		log.info("PaymentPlan actualizado exitosamente. ID: {}", id);

		return convertToDTO(paymentPlan);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasActiveSubscription(Long businessId) {
		return paymentPlanRepository.existsActiveByBusinessId(businessId);
	}

	@Override
	public String getMercadoPagoUrl() {
		return MERCADO_PAGO_URL;
	}

	@Override
	@Transactional
	public PaymentPlanDTO markPaymentAsPending(Long id, String paymentMethod) {
		log.info("Marcando pago como pendiente para PaymentPlan ID: {}", id);

		PaymentPlan paymentPlan = paymentPlanRepository.findById(id).orElseThrow(() -> new RuntimeException("PaymentPlan no encontrado con id: " + id));

		paymentPlan.setStatus(PaymentStatus.PENDING);
		paymentPlan.setPaymentMethod(paymentMethod);

		paymentPlan = paymentPlanRepository.save(paymentPlan);
		log.info("Pago marcado como pendiente. ID: {}, Método: {}", id, paymentMethod);

		return convertToDTO(paymentPlan);
	}

	@Override
	@Transactional(readOnly = true)
	@Scheduled(cron = "0 0 9 * * ?") // Every day at 9:00 AM
	public void checkAndUpdateExpiredTrials() {
		log.info("Verificando trials expirados...");

		var expiredTrials = paymentPlanRepository.findExpiredTrials();

		expiredTrials.forEach(trial -> {
			trial.setStatus(PaymentStatus.EXPIRED);
			
			paymentPlanRepository.save(trial);
			
			log.info("Trial expirado para businessId: {}", trial.getBusinessId());
			// Send notification to the User
		});
		
		log.info("Actualizados {} trials expirados", expiredTrials.size());
	}

	private String generateExternalReference(Long businessId) {
		return String.format("TRAELO-%d-%s", businessId, UUID.randomUUID().toString().substring(0, 8).toUpperCase());
	}

	private PaymentPlanDTO convertToDTO(PaymentPlan paymentPlan) {
		PaymentPlanDTO dto = new PaymentPlanDTO();
		dto.setId(paymentPlan.getId());
		dto.setBusinessId(paymentPlan.getBusinessId());
		dto.setUserId(paymentPlan.getUserId());
		dto.setPlanType(paymentPlan.getPlanType().name());
		dto.setStatus(paymentPlan.getStatus().name());
		dto.setExternalReference(paymentPlan.getExternalReference());
		dto.setAmountMxn(paymentPlan.getAmountMxn());
		dto.setTrialDays(paymentPlan.getTrialDays());
		dto.setTrialStart(paymentPlan.getTrialStart());
		dto.setTrialEnd(paymentPlan.getTrialEnd());
		dto.setNextBillingDate(paymentPlan.getNextBillingDate());
		dto.setLastPaymentDate(paymentPlan.getLastPaymentDate());
		dto.setPaymentMethod(paymentPlan.getPaymentMethod());
		dto.setCreatedAt(paymentPlan.getCreatedAt());

		// Calculated fields
		dto.setIsActive(paymentPlan.isActive());
		dto.setIsTrial(paymentPlan.isTrial());
		dto.setDaysRemaining(paymentPlan.getDaysRemaining());
		dto.setHasActiveSubscription(paymentPlan.isActive());
		dto.setCanActivate(paymentPlan.getStatus() == PaymentStatus.TRIAL && paymentPlan.getDaysRemaining() > 0);
		dto.setMercadoPagoUrl(MERCADO_PAGO_URL); // Fixed URL for everyone

		return dto;
	}
}
