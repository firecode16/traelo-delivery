package com.traelo.delivery.model.dto;

import lombok.Data;

@Data
public class CreatePaymentPlanDTO {
	private Long businessId;
	private Long userId;
	private String businessName;
}
