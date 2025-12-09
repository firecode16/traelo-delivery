package com.traelo.delivery.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRequestDTO {
	private Long businessId;
	private Long userId;
	private String fullName;
	private String description;
	private String address;
	private Double latitude;
	private Double longitude;
	private byte[] backdrop;
	private Boolean isActive;
	private Boolean acceptCash;
	private Boolean acceptTransfer;
	private String bankClabe;
	private String bankCard;
	private String createdAt;
	private String updatedAt;
	private SectorDTO sector;
}
