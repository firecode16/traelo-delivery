package com.traelo.delivery.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusinessDashboardDTO {
	private BusinessRequestDTO business;
	private SectorDTO sector;
	private List<DeliveryZoneDTO> deliveryZones;
	private List<ZoneCommissionResponseDTO> zoneCommissions;
}
