package com.traelo.delivery.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private Long productId;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "base_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal basePrice;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	@Column(name = "general_stock")
	private Integer generalStock;

	@Column(name = "image_urls", columnDefinition = "JSON")
	private String imageUrls;

	@Column(name = "attributes", columnDefinition = "JSON")
	private String attributes;

	@Column(name = "requires_prescription")
	private Boolean requiresPrescription;

	@Column(name = "preparation_time_minutes")
	private Integer preparationTimeMinutes;

	@Column(name = "unit_of_sale", length = 50)
	private String unitOfSale;

	@Column(name = "brand", length = 100)
	private String brand;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductVariant> variants;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id", nullable = false)
	private Business business; // FK a Business

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sector_id", nullable = false)
	private Sector sector; // FK to Sector

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category; // FK to Category
}
