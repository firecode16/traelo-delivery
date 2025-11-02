package com.traelo.delivery.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.traelo.delivery.enums.ShippingType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneCommissionResponseDTO {
	private Long businessAuxId;
	private Long zoneCommissionId;
	private ShippingType shippingType;
	private String selectedOption;
	private BigDecimal commissionAmount;
	private String address;
	private Object coordinates;
	private Long deliveryZoneId;
	private Date createdAt;
	private Date updatedAt;
}
