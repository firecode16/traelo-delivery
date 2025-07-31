package com.traelo.delivery.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
	private Long menuId;
	private Long businessId;
	private String name;
	private String description;
	private String category;
	private BigDecimal price;
	private Boolean isActive;
}
