package com.traelo.delivery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZoneInfoDTO {
	private String zoneId;
	private String zoneName;
	private String deliveryZoneName;
	private Double latitude;
	private Double longitude;
	private String address;
}
