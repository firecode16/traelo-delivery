package com.traelo.delivery.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.DeliveryZone;
import com.traelo.delivery.model.dto.DeliveryZoneDTO;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.DeliveryZoneRepository;
import com.traelo.delivery.service.DeliveryZoneService;

@Service
public class DeliveryZoneServiceImpl implements DeliveryZoneService {
	@Autowired
	private DeliveryZoneRepository deliveryZoneRepository;
	@Autowired
	private BusinessRepository businessRepository;

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
}
