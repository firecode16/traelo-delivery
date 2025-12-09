package com.traelo.delivery.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneCommissionUpdateDTO {
	private Long businessAuxId;
	private Long zoneCommissionId;
	private String selectedOption;
	private BigDecimal commissionAmount;
	private Date updatedAt;
}
