package com.traelo.delivery.model;

import java.math.BigDecimal;
import java.util.Date;

import com.traelo.delivery.enums.ShippingType;
import com.traelo.delivery.util.JsonConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zone_commissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneCommission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private Long businessAuxId;
	@Column
	private Long zoneCommissionId;

	@Enumerated(EnumType.STRING)
	@Column(name = "shipping_type", nullable = false, length = 20)
	private ShippingType shippingType;

	@Column
	private String selectedOption;

	@Column(name = "commission_amount", precision = 10, scale = 2)
	private BigDecimal commissionAmount;

	@Column
	private String address;

	@Column(name = "coordinates", columnDefinition = "JSON") // for GeoJSON (polygon)
	@Convert(converter = JsonConverter.class)
	private Object coordinates;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_zone_id", nullable = false)
	private DeliveryZone deliveryZone; // FK to DeliveryZone

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id", nullable = false)
	private Business business; // FK a Business
}
