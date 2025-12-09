package com.traelo.delivery.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UpdatePaymentPlanDTO {
	private String status;
	private LocalDateTime lastPaymentDate;
	private String paymentMethod;
	private String paymentProofUrl;
	private String notes;
}
