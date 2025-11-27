package com.traelo.delivery.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Long productId;
	private Long sectorId;
	private Long businessId;
	private String sectorName;
	private String name;
	private String description;
	private BigDecimal price;
	private boolean isActive;
	private String category;
	private Integer generalStock;
	private String ingredients;
	private Integer preparationTime;
	private String brand;
	private List<ProductVariantDTO> variants;
}
