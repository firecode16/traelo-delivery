package com.traelo.delivery.service;

import java.util.List;

import com.traelo.delivery.model.dto.ZoneCommissionDTO;
import com.traelo.delivery.model.dto.ZoneCommissionResponseDTO;
import com.traelo.delivery.model.dto.ZoneCommissionUpdateDTO;

public interface ZoneCommissionService {
	List<ZoneCommissionResponseDTO> createZoneCommissions(List<ZoneCommissionDTO> commissions);

	ZoneCommissionResponseDTO createZoneCommission(ZoneCommissionDTO zoneCommissionDTO);

	List<ZoneCommissionResponseDTO> getZoneCommissionsByBusinessId(Long businessId);

	List<ZoneCommissionResponseDTO> getZoneCommissionsByBusinessAndZone(Long businessId, Long deliveryZoneId);

	void updateZoneCommissionOptions(List<ZoneCommissionUpdateDTO> zCommissionUpdateDTO);
}
