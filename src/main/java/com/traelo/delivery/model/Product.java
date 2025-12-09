package com.traelo.delivery.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.traelo.delivery.util.JsonConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "base_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal basePrice;

	@Column(name = "is_active")
	private boolean isActive = true;

	@Column(name = "general_stock")
	private Integer generalStock;

	@Column(name = "attributes", columnDefinition = "JSON")
	@Convert(converter = JsonConverter.class)
	private Map<String, String> attributes;

	@Column(name = "requires_prescription") // For pharmacy
	private Boolean requiresPrescription;

	@Column(name = "preparation_time_minutes") // For food
	private Integer preparationTimeMinutes;

	@Column(name = "unit_of_sale", length = 50) // For hardware
	private String unitOfSale;

	@Column(name = "brand", length = 100) // For fashion, technology, hardware, pharmacy
	private String brand;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
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
