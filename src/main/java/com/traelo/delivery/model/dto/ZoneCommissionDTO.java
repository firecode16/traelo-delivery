package com.traelo.delivery.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneCommissionDTO {
	private Long businessAuxId;
	private Long zoneCommissionId;
	private String shippingType;
	private String selectedOption;
	private BigDecimal commissionAmount;
	private String address;
	private Object coordinates;
	private Long deliveryZoneId;
	private String createdAt;
	private String updatedAt;
}
