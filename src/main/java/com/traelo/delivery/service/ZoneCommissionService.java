package com.traelo.delivery.service;

import java.util.List;

import com.traelo.delivery.model.dto.ZoneCommissionDTO;
import com.traelo.delivery.model.dto.ZoneCommissionResponseDTO;

public interface ZoneCommissionService {
	List<ZoneCommissionResponseDTO> createZoneCommissions(List<ZoneCommissionDTO> commissions);

	ZoneCommissionResponseDTO createZoneCommission(ZoneCommissionDTO zoneCommissionDTO);

	List<ZoneCommissionResponseDTO> getZoneCommissionsByBusinessId(Long businessId);
}
