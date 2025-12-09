package com.traelo.delivery.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessUpdateDTO {
	private String fullName;
	private String description;
	private String address;
	private Boolean isActive;
	private String updatedAt;
}
