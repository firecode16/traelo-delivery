package com.traelo.delivery.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traelo.delivery.model.dto.ActivateSubscriptionDTO;
import com.traelo.delivery.model.dto.CreatePaymentPlanDTO;
import com.traelo.delivery.model.dto.PaymentPlanDTO;
import com.traelo.delivery.model.dto.UpdatePaymentPlanDTO;
import com.traelo.delivery.service.PaymentPlanService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payment-plans")
@Slf4j
public class PaymentPlanController {
	@Autowired
	private PaymentPlanService paymentPlanService;

	@PostMapping("/createSubscription")
	public ResponseEntity<?> createPaymentPlan(@RequestBody CreatePaymentPlanDTO request) {
		try {
			log.info("POST /api/payment-plans - Creando PaymentPlan para businessId: {}", request.getBusinessId());

			PaymentPlanDTO paymentPlanDTO = paymentPlanService.createPaymentPlan(request);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Plan de pago creado exitosamente");
			response.put("data", paymentPlanDTO);

			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			log.error("Error creando PaymentPlan: {}", e.getMessage(), e);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al crear plan de pago");
			errorResponse.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@GetMapping("/getPaymentPlanByBusiness/{businessId}")
	public ResponseEntity<?> getPaymentPlanByBusinessId(@PathVariable Long businessId) {
		try {
			log.info("GET /api/payment-plans/business/{}", businessId);

			PaymentPlanDTO paymentPlanDTO = paymentPlanService.getPaymentPlanByBusinessId(businessId);

			return ResponseEntity.ok(paymentPlanDTO);
		} catch (Exception e) {
			log.error("Error obteniendo PaymentPlan: {}", e.getMessage(), e);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al obtener plan de pago");
			errorResponse.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@PutMapping("/{id}/activate")
	public ResponseEntity<?> activatePaymentPlan(@PathVariable Long id, @RequestBody ActivateSubscriptionDTO request) {
		try {
			log.info("PUT /api/payment-plans/{}/activate", id);

			PaymentPlanDTO paymentPlanDTO = paymentPlanService.activatePaymentPlan(id, request);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Suscripción activada exitosamente");
			response.put("data", paymentPlanDTO);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error activando PaymentPlan: {}", e.getMessage(), e);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al activar suscripción");
			errorResponse.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<?> cancelPaymentPlan(@PathVariable Long id) {
		try {
			log.info("PUT /api/payment-plans/{}/cancel", id);

			PaymentPlanDTO paymentPlanDTO = paymentPlanService.cancelPaymentPlan(id);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Suscripción cancelada exitosamente");
			response.put("data", paymentPlanDTO);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error cancelando PaymentPlan: {}", e.getMessage(), e);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al cancelar suscripción");
			errorResponse.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePaymentPlan(@PathVariable Long id, @RequestBody UpdatePaymentPlanDTO request) {
		try {
			log.info("PUT /api/payment-plans/{}", id);

			PaymentPlanDTO paymentPlanDTO = paymentPlanService.updatePaymentPlan(id, request);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Plan actualizado exitosamente");
			response.put("data", paymentPlanDTO);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error actualizando PaymentPlan: {}", e.getMessage(), e);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al actualizar plan");
			errorResponse.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@GetMapping("/{businessId}/hasActive")
	public ResponseEntity<?> hasActiveSubscription(@PathVariable Long businessId) {
		try {
			log.info("GET /api/payment-plans/business/{}/has-active", businessId);

			boolean hasActive = paymentPlanService.hasActiveSubscription(businessId);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("businessId", businessId);
			response.put("hasActiveSubscription", hasActive);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error verificando suscripción activa: {}", e.getMessage(), e);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al verificar suscripción");
			errorResponse.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("/getMercadoPagoUrl")
	public ResponseEntity<?> getMercadoPagoUrl() {
		try {
			String url = paymentPlanService.getMercadoPagoUrl();

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("mercadoPagoUrl", url);
			response.put("message", "URL obtenida exitosamente");

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error obteniendo URL de Mercado Pago: {}", e.getMessage(), e);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al obtener URL");
			errorResponse.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/{id}/markPending")
	public ResponseEntity<?> markPaymentAsPending(@PathVariable Long id, @RequestBody Map<String, String> request) {
		try {
			log.info("POST /api/payment-plans/{}/mark-pending", id);

			String paymentMethod = request.get("paymentMethod");
			PaymentPlanDTO paymentPlanDTO = paymentPlanService.markPaymentAsPending(id, paymentMethod);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Pago marcado como pendiente. Verificaremos manualmente.");
			response.put("data", paymentPlanDTO);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error marcando pago como pendiente: {}", e.getMessage(), e);

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("error", "Error al marcar pago");
			errorResponse.put("message", e.getMessage());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@GetMapping("/health")
	public ResponseEntity<?> healthCheck() {
		Map<String, Object> response = new HashMap<>();
		response.put("status", "UP");
		response.put("service", "PaymentPlanService");
		response.put("timestamp", System.currentTimeMillis());

		return ResponseEntity.ok(response);
	}
}
