package com.traelo.delivery.model.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DeliveryZoneUpdateRequestDTO {
	private Long businessAuxId;
	private Boolean homeDeliveryEnabled;
	private Boolean pickupEnabled;
	private Boolean deliveryCentersEnabled;
	private List<ZoneDTO> zones;
	private List<PointDTO> points;
	private List<Long> deletedZones;
	private List<Long> deletedPoints;
	private List<ZoneCommissionDTO> commissions;
}
