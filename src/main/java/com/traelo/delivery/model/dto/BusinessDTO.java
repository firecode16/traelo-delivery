package com.traelo.delivery.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDTO {
	private Long businessId;
	private Long userId;
	private String phone;
	private String fullName;
	private String description;
	private String address;
	private Boolean isActive;

	private List<MenuDTO> menu;
	private SchedulerDTO scheduler;
}
