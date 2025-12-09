package com.traelo.delivery.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethodDTO {
	private Long businessId;
	private Boolean acceptCash;
	private Boolean acceptTransfer;
	private String bankCard;
	private String bankClabe;
	private String updatedAt;
}
