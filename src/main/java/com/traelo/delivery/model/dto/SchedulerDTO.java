package com.traelo.delivery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerDTO {
	private Long schedulerId;
	private Long businessId;
	private Boolean isActive;
}
