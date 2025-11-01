package com.traelo.delivery.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectorDTO {
	private Long sectorId;
	private String name;
	private String displayNameProductTab;
	private String iconName;
	private boolean isActive = true;
}
