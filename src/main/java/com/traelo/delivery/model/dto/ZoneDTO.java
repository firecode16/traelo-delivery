package com.traelo.delivery.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneDTO {
	private String id;
	private String name;
	private String place_name;
	private CenterDTO center;
	private String address;
	private Object geometry;
}
