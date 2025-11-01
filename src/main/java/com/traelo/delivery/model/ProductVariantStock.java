package com.traelo.delivery.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_variant_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantStock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private Long productVariantStockId;

	@Column(name = "variant_combination_description", nullable = false, length = 255)
	private String variantCombinationDescription;

	@Column(name = "stock", nullable = false)
	private Integer stock;

	@Column(name = "additional_price", precision = 10, scale = 2)
	private BigDecimal additionalPrice;

	@Column(name = "sku", length = 50, unique = true)
	private String sku;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product; // FK to Product
}
