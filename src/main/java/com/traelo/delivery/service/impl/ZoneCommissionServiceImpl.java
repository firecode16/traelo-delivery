package com.traelo.delivery.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.enums.ShippingType;
import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.DeliveryZone;
import com.traelo.delivery.model.ZoneCommission;
import com.traelo.delivery.model.dto.ZoneCommissionDTO;
import com.traelo.delivery.model.dto.ZoneCommissionResponseDTO;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.DeliveryZoneRepository;
import com.traelo.delivery.repository.ZoneCommissionRepository;
import com.traelo.delivery.service.ZoneCommissionService;

@Service
public class ZoneCommissionServiceImpl implements ZoneCommissionService {
	@Autowired
	private ZoneCommissionRepository zoneCommissionRepository;
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private DeliveryZoneRepository deliveryZoneRepository;

	@Transactional
	@Override
	public List<ZoneCommissionResponseDTO> createZoneCommissions(List<ZoneCommissionDTO> commissions) {
		List<ZoneCommissionResponseDTO> savedCommissions = new ArrayList<>();

		if (commissions == null || commissions.isEmpty()) {
			return savedCommissions;
		}

		System.out.println("=== INICIANDO PROCESAMIENTO DE COMISIONES ===");
		System.out.println("Total de comisiones recibidas: " + commissions.size());

		AtomicInteger counter = new AtomicInteger(1);

		commissions.forEach(zCommissionDTO -> {
			try {
				int currentIndex = counter.getAndIncrement();
				System.out.println("--- Procesando comisión " + currentIndex + " ---");

				Business business = businessRepository.findByBusinessId(zCommissionDTO.getBusinessAuxId()).orElseThrow(() -> new RuntimeException("Business not found with id: " + zCommissionDTO.getBusinessAuxId()));
				DeliveryZone deliveryZone = deliveryZoneRepository.findByDeliveryZoneId(zCommissionDTO.getDeliveryZoneId()).orElseThrow(() -> new RuntimeException("DeliveryZone not found with id: " + zCommissionDTO.getDeliveryZoneId()));
				ShippingType shippingType = ShippingType.valueOf(zCommissionDTO.getShippingType());

				ZoneCommission zoneCommission = new ZoneCommission();
				zoneCommission.setBusinessAuxId(zCommissionDTO.getBusinessAuxId());
				zoneCommission.setZoneCommissionId(generateUniqueZoneCommissionId());
				zoneCommission.setShippingType(shippingType);
				zoneCommission.setSelectedOption(zCommissionDTO.getSelectedOption());
				zoneCommission.setCommissionAmount(zCommissionDTO.getCommissionAmount());
				zoneCommission.setAddress(zCommissionDTO.getAddress());
				zoneCommission.setCoordinates(zCommissionDTO.getCoordinates());
				zoneCommission.setBusiness(business);
				zoneCommission.setDeliveryZone(deliveryZone);
				zoneCommission.setCreatedAt(new Date());
				zoneCommission.setUpdatedAt(new Date());

				ZoneCommission savedCommission = zoneCommissionRepository.save(zoneCommission);
				savedCommissions.add(convertToResponseDTO(savedCommission));
				System.out.println("Commission guardada - ID: " + savedCommission.getId());

			} catch (DataIntegrityViolationException e) {
				System.err.println("❌ ERROR DE DUPLICADO - Intentando con ID único diferente...");
				retryWithNewId(zCommissionDTO, savedCommissions);
			} catch (Exception e) {
				System.err.println("❌ ERROR PROCESANDO COMISIÓN: " + e.getMessage());
				e.printStackTrace();
			}
		});

		System.out.println("=== PROCESAMIENTO COMPLETADO ===");
		System.out.println("Total de comisiones guardadas: " + savedCommissions.size());

		System.out.println("=== RESUMEN FINAL ===");
		savedCommissions.forEach(dto -> {
			System.out.println("Commission: Business=" + dto.getBusinessAuxId() + ", Zone=" + dto.getDeliveryZoneId() + ", Type=" + dto.getShippingType() + ", Amount=" + dto.getCommissionAmount() + ", Address=" + dto.getAddress());
		});

		return savedCommissions;
	}

	private void retryWithNewId(ZoneCommissionDTO zCommissionDTO, List<ZoneCommissionResponseDTO> savedCommissions) {
		try {
			System.out.println("REINTENTANDO CON NUEVO ID ÚNICO...");

			Business business = businessRepository.findByBusinessId(zCommissionDTO.getBusinessAuxId()).orElseThrow(() -> new RuntimeException("Business not found with id: " + zCommissionDTO.getBusinessAuxId()));
			DeliveryZone deliveryZone = deliveryZoneRepository.findByDeliveryZoneId(zCommissionDTO.getDeliveryZoneId()).orElseThrow(() -> new RuntimeException("DeliveryZone not found with id: " + zCommissionDTO.getDeliveryZoneId()));
			ShippingType shippingType = ShippingType.valueOf(zCommissionDTO.getShippingType());

			ZoneCommission zoneCommission = new ZoneCommission();
			zoneCommission.setBusinessAuxId(zCommissionDTO.getBusinessAuxId());
			zoneCommission.setZoneCommissionId(generateUniqueZoneCommissionId());
			zoneCommission.setShippingType(shippingType);
			zoneCommission.setSelectedOption(zCommissionDTO.getSelectedOption());
			zoneCommission.setCommissionAmount(zCommissionDTO.getCommissionAmount());
			zoneCommission.setAddress(zCommissionDTO.getAddress());
			zoneCommission.setCoordinates(zCommissionDTO.getCoordinates());
			zoneCommission.setBusiness(business);
			zoneCommission.setDeliveryZone(deliveryZone);
			zoneCommission.setCreatedAt(new Date());
			zoneCommission.setUpdatedAt(new Date());

			ZoneCommission savedCommission = zoneCommissionRepository.save(zoneCommission);
			savedCommissions.add(convertToResponseDTO(savedCommission));
			System.out.println("COMISIÓN GUARDADA EN REINTENTO - ID: " + savedCommission.getId());

		} catch (Exception ex) {
			System.err.println("❌ ERROR EN REINTENTO: " + ex.getMessage());
		}
	}

	@Transactional
	@Override
	public ZoneCommissionResponseDTO createZoneCommission(ZoneCommissionDTO zoneCommissionDTO) {
		Business business = businessRepository.findByBusinessId(zoneCommissionDTO.getBusinessAuxId()).orElseThrow(() -> new RuntimeException("Business not found with id: " + zoneCommissionDTO.getBusinessAuxId()));
		DeliveryZone deliveryZone = deliveryZoneRepository.findByDeliveryZoneId(zoneCommissionDTO.getDeliveryZoneId()).orElseThrow(() -> new RuntimeException("DeliveryZone not found with id: " + zoneCommissionDTO.getDeliveryZoneId()));
		ShippingType shippingType = ShippingType.valueOf(zoneCommissionDTO.getShippingType());

		ZoneCommission zoneCommission = new ZoneCommission();
		zoneCommission.setBusinessAuxId(zoneCommissionDTO.getBusinessAuxId());
		zoneCommission.setZoneCommissionId(generateUniqueZoneCommissionId());
		zoneCommission.setShippingType(shippingType);
		zoneCommission.setSelectedOption(zoneCommissionDTO.getSelectedOption());
		zoneCommission.setCommissionAmount(zoneCommissionDTO.getCommissionAmount());
		zoneCommission.setAddress(zoneCommissionDTO.getAddress());
		zoneCommission.setCoordinates(zoneCommissionDTO.getCoordinates());
		zoneCommission.setBusiness(business);
		zoneCommission.setDeliveryZone(deliveryZone);
		zoneCommission.setCreatedAt(new Date());
		zoneCommission.setUpdatedAt(new Date());

		ZoneCommission savedCommission = zoneCommissionRepository.save(zoneCommission);
		return convertToResponseDTO(savedCommission);
	}

	private Long generateUniqueZoneCommissionId() {
		return System.currentTimeMillis() + (long) (Math.random() * 10000) + Thread.currentThread().getId();
	}

	@Transactional(readOnly = true)
	@Override
	public List<ZoneCommissionResponseDTO> getZoneCommissionsByBusinessId(Long businessId) {
		List<ZoneCommission> commissions = zoneCommissionRepository.findByBusinessBusinessId(businessId);
		return commissions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<ZoneCommissionResponseDTO> getZoneCommissionsByBusinessAndZone(Long businessId, Long deliveryZoneId) {
		List<ZoneCommission> commissions = zoneCommissionRepository.findByBusinessBusinessIdAndDeliveryZoneDeliveryZoneId(businessId, deliveryZoneId);
		return commissions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	private ZoneCommissionResponseDTO convertToResponseDTO(ZoneCommission zoneCommission) {
		ZoneCommissionResponseDTO dto = new ZoneCommissionResponseDTO();
		dto.setBusinessAuxId(zoneCommission.getBusinessAuxId());
		dto.setZoneCommissionId(zoneCommission.getZoneCommissionId());
		dto.setShippingType(zoneCommission.getShippingType());
		dto.setSelectedOption(zoneCommission.getSelectedOption());
		dto.setCommissionAmount(zoneCommission.getCommissionAmount());
		dto.setAddress(zoneCommission.getAddress());
		dto.setCoordinates(zoneCommission.getCoordinates());
		dto.setDeliveryZoneId(zoneCommission.getDeliveryZone().getDeliveryZoneId());
		dto.setCreatedAt(zoneCommission.getCreatedAt());
		dto.setUpdatedAt(zoneCommission.getUpdatedAt());
		return dto;
	}
}