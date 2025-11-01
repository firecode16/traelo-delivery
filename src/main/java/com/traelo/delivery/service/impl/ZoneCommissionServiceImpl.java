package com.traelo.delivery.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

		// Get business and delivery zone from first commission (assuming all are same)
		ZoneCommissionDTO firstCommission = commissions.get(0);
		Business business = businessRepository.findByBusinessId(firstCommission.getBusinessId()).orElseThrow(() -> new RuntimeException("Business not found"));
		DeliveryZone deliveryZone = deliveryZoneRepository.findByDeliveryZoneId(firstCommission.getDeliveryZoneId()).orElseThrow(() -> new RuntimeException("DeliveryZone not found"));

		for (ZoneCommissionDTO zCommissionDTO : commissions) {
			ZoneCommission zoneCommission = new ZoneCommission();
			zoneCommission.setZoneCommissionId(zCommissionDTO.getZoneCommissionId());
			zoneCommission.setShippingType(ShippingType.valueOf(zCommissionDTO.getShippingType()));
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
		}

		return savedCommissions;
	}

	@Transactional
	@Override
	public ZoneCommissionResponseDTO createZoneCommission(ZoneCommissionDTO zoneCommissionDTO) {
		Business business = businessRepository.findByBusinessId(zoneCommissionDTO.getBusinessId()).orElseThrow(() -> new RuntimeException("Business not found"));
		DeliveryZone deliveryZone = deliveryZoneRepository.findByDeliveryZoneId(zoneCommissionDTO.getDeliveryZoneId()).orElseThrow(() -> new RuntimeException("DeliveryZone not found"));

		ZoneCommission zoneCommission = new ZoneCommission();
		zoneCommission.setZoneCommissionId(zoneCommissionDTO.getZoneCommissionId());
		zoneCommission.setShippingType(ShippingType.valueOf(zoneCommissionDTO.getShippingType()));
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

	@Transactional(readOnly = true)
	@Override
	public List<ZoneCommissionResponseDTO> getZoneCommissionsByBusinessId(Long businessId) {
		List<ZoneCommission> commissions = zoneCommissionRepository.findByBusinessBusinessId(businessId);
		return commissions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	private ZoneCommissionResponseDTO convertToResponseDTO(ZoneCommission zoneCommission) {
		ZoneCommissionResponseDTO dto = new ZoneCommissionResponseDTO();
		dto.setZoneCommissionId(zoneCommission.getZoneCommissionId());
		dto.setShippingType(zoneCommission.getShippingType());
		dto.setSelectedOption(zoneCommission.getSelectedOption());
		dto.setCommissionAmount(zoneCommission.getCommissionAmount());
		dto.setAddress(zoneCommission.getAddress());
		dto.setCoordinates(zoneCommission.getCoordinates());
		dto.setBusinessId(zoneCommission.getBusiness().getBusinessId());
		dto.setDeliveryZoneId(zoneCommission.getDeliveryZone().getDeliveryZoneId());
		dto.setCreatedAt(zoneCommission.getCreatedAt());
		dto.setUpdatedAt(zoneCommission.getUpdatedAt());
		return dto;
	}
}