package com.traelo.delivery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {
	private String id;
	private String name;
	private String place_name;
	private CenterDTO center;
	private String address;
	private GeometryDTO geometry;
}
