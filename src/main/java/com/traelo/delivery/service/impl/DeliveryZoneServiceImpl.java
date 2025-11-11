package com.traelo.delivery.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.DeliveryZone;
import com.traelo.delivery.model.ZoneCommission;
import com.traelo.delivery.model.dto.DeliveryZoneDTO;
import com.traelo.delivery.model.dto.DeliveryZoneUpdateRequestDTO;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.DeliveryZoneRepository;
import com.traelo.delivery.repository.ZoneCommissionRepository;
import com.traelo.delivery.service.DeliveryZoneService;

@Service
public class DeliveryZoneServiceImpl implements DeliveryZoneService {
	@Autowired
	private DeliveryZoneRepository deliveryZoneRepository;
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private ZoneCommissionRepository zoneCommissionRepository;

	@Transactional
	@Override
	public DeliveryZoneDTO createDeliveryZone(DeliveryZoneDTO deliveryZoneDTO) {
		try {
			Business business = businessRepository.findByBusinessId(deliveryZoneDTO.getBusinessAuxId()).orElseThrow(() -> new RuntimeException("Business not found"));

			DeliveryZone deliveryZone = new DeliveryZone();
			deliveryZone.setBusinessAuxId(deliveryZoneDTO.getBusinessAuxId());
			deliveryZone.setDeliveryZoneId(deliveryZoneDTO.getDeliveryZoneId());
			deliveryZone.setZoneName(deliveryZoneDTO.getZoneName());
			deliveryZone.setPickupEnabled(deliveryZoneDTO.getPickupEnabled());
			deliveryZone.setHomeDeliveryEnabled(deliveryZoneDTO.getHomeDeliveryEnabled());
			deliveryZone.setDeliveryCentersEnabled(deliveryZoneDTO.getDeliveryCentersEnabled());
			deliveryZone.setZones(deliveryZoneDTO.getZones());
			deliveryZone.setPoints(deliveryZoneDTO.getPoints());
			deliveryZone.setActive(deliveryZoneDTO.getIsActive());
			deliveryZone.setCreatedAt(new Date());
			deliveryZone.setUpdatedAt(new Date());
			deliveryZone.setBusiness(business);

			DeliveryZone saved = deliveryZoneRepository.save(deliveryZone);
			return convertToResponseDTO(saved);
		} catch (Exception e) {
			System.err.println("❌ Error al guardar DeliveryZone: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error creating delivery zone: " + e.getMessage(), e);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<DeliveryZone> getDeliveryZonesByBusinessId(Long businessId) {
		return deliveryZoneRepository.findByBusinessBusinessId(businessId);
	}

	private DeliveryZoneDTO convertToResponseDTO(DeliveryZone deliveryZone) {
		DeliveryZoneDTO dto = new DeliveryZoneDTO();
		dto.setBusinessAuxId(deliveryZone.getBusinessAuxId());
		dto.setDeliveryZoneId(deliveryZone.getDeliveryZoneId());
		dto.setZoneName(deliveryZone.getZoneName());
		dto.setPickupEnabled(deliveryZone.getPickupEnabled());
		dto.setHomeDeliveryEnabled(deliveryZone.getHomeDeliveryEnabled());
		dto.setDeliveryCentersEnabled(deliveryZone.isDeliveryCentersEnabled());
		dto.setZones(deliveryZone.getZones());
		dto.setPoints(deliveryZone.getPoints());
		dto.setIsActive(deliveryZone.isActive());
		dto.setCreatedAt(deliveryZone.getCreatedAt().toString());
		dto.setUpdatedAt(deliveryZone.getUpdatedAt().toString());
		return dto;
	}

	@Transactional
	@Override
	public void updateDeliveryZoneOptions(Long businessId, DeliveryZoneUpdateRequestDTO dZoneUpdateRequestDTO) {
		try {
			List<DeliveryZone> existingZones = deliveryZoneRepository.findByBusinessBusinessId(businessId);
			DeliveryZone deliveryZone;

			if (existingZones.isEmpty()) {
				Business business = businessRepository.findByBusinessId(businessId).orElseThrow(() -> new RuntimeException("Business not found"));

				deliveryZone = new DeliveryZone();
				deliveryZone.setBusinessAuxId(businessId);
				deliveryZone.setBusiness(business);
				deliveryZone.setCreatedAt(new Date());
			} else {
				deliveryZone = existingZones.get(0);
			}

			deliveryZone.setHomeDeliveryEnabled(dZoneUpdateRequestDTO.getHomeDeliveryEnabled());
			deliveryZone.setPickupEnabled(dZoneUpdateRequestDTO.getPickupEnabled());
			deliveryZone.setDeliveryCentersEnabled(dZoneUpdateRequestDTO.getDeliveryCentersEnabled());
			deliveryZone.setZones(dZoneUpdateRequestDTO.getZones());
			deliveryZone.setPoints(dZoneUpdateRequestDTO.getPoints());
			deliveryZone.setUpdatedAt(new Date());

			DeliveryZone savedZone = deliveryZoneRepository.save(deliveryZone);

			// 🆕 Sync deletions first
			handleDeletedZonesAndPoints(businessId, dZoneUpdateRequestDTO);

			syncCommissionsWithDeliveryOptions(businessId, dZoneUpdateRequestDTO, savedZone);
		} catch (Exception e) {
			System.err.println("❌ Error updating delivery zone options: " + e.getMessage());
			throw new RuntimeException("Error updating delivery zone options: " + e.getMessage(), e);
		}
	}

	private void handleDeletedZonesAndPoints(Long businessId, DeliveryZoneUpdateRequestDTO dZoneUpdateRequestDTO) {
		try {
			// Remove commissions from deleted zones
			if (dZoneUpdateRequestDTO.getDeletedZones() != null && !dZoneUpdateRequestDTO.getDeletedZones().isEmpty()) {
				System.out.println("🗑️ Eliminando comisiones de zonas: " + dZoneUpdateRequestDTO.getDeletedZones());

				for (Long zoneCommissionId : dZoneUpdateRequestDTO.getDeletedZones()) {
					zoneCommissionRepository.deleteByZoneCommissionIdAndBusinessAuxId(zoneCommissionId, businessId);
				}
			}

			// Remove commissions from deleted points
			if (dZoneUpdateRequestDTO.getDeletedPoints() != null && !dZoneUpdateRequestDTO.getDeletedPoints().isEmpty()) {
				System.out.println("🗑️ Eliminando comisiones de puntos: " + dZoneUpdateRequestDTO.getDeletedPoints());

				for (Long pointCommissionId : dZoneUpdateRequestDTO.getDeletedPoints()) {
					zoneCommissionRepository.deleteByZoneCommissionIdAndBusinessAuxId(pointCommissionId, businessId);
				}
			}
		} catch (Exception e) {
			System.err.println("❌ Error eliminando zonas/puntos: " + e.getMessage());
			throw new RuntimeException("Error eliminando zonas/puntos: " + e.getMessage(), e);
		}
	}

	private void syncCommissionsWithDeliveryOptions(Long businessId, DeliveryZoneUpdateRequestDTO dZoneUpdateRequestDTO, DeliveryZone savedZone) {
		// Get all commissions business
		List<ZoneCommission> commissions = zoneCommissionRepository.findByBusinessAuxId(businessId);

		for (ZoneCommission commission : commissions) {
			String shippingType = commission.getShippingType().name();
			boolean shouldBeActive = false;

			if ("DELIVERY".equals(shippingType)) {
				shouldBeActive = Boolean.TRUE.equals(dZoneUpdateRequestDTO.getHomeDeliveryEnabled());
			} else if ("PICKUP".equals(shippingType)) {
				shouldBeActive = Boolean.TRUE.equals(dZoneUpdateRequestDTO.getDeliveryCentersEnabled());
			}

			commission.setActive(shouldBeActive);
			commission.setUpdatedAt(new Date());
			commission.setDeliveryZone(savedZone);

			zoneCommissionRepository.save(commission);
		}
	}

}
