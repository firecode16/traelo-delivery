package com.traelo.delivery.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryZoneDTO {
	private Long id;
	private Long businessAuxId;
	private Long deliveryZoneId;
	private String zoneName;
	private Boolean pickupEnabled;
	private Boolean homeDeliveryEnabled;
	private Boolean deliveryCentersEnabled;
	private List<ZoneDTO> zones;
	private List<PointDTO> points;
	private Boolean isActive;
	private String createdAt;
	private String updatedAt;
}
