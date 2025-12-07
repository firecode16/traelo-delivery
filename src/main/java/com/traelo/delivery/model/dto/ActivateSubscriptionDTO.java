package com.traelo.delivery.model.dto;

import lombok.Data;

@Data
public class ActivateSubscriptionDTO {
	private String paymentMethod;
	private String paymentProofUrl; // Receipt URL
	private String notes;
}
