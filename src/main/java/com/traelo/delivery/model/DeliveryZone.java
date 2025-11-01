package com.traelo.delivery.model;

import java.util.Date;
import java.util.List;

import com.traelo.delivery.model.dto.PointDTO;
import com.traelo.delivery.model.dto.ZoneDTO;
import com.traelo.delivery.util.JsonConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
@Table(name = "delivery_zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryZone {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private Long deliveryZoneId;

	@Column(name = "zone_name", nullable = false, length = 100)
	private String zoneName;

	@Column
	private Boolean pickupEnabled = true;

	@Column
	private Boolean homeDeliveryEnabled = false;

	@Column
	private boolean deliveryCentersEnabled = false;

	@Column(name = "zones", columnDefinition = "JSON") // for GeoJSON (polygon)
	@Convert(converter = JsonConverter.class)
	private List<ZoneDTO> zones;

	@Column(name = "points", columnDefinition = "JSON") // for GeoJSON (polygon)
	@Convert(converter = JsonConverter.class)
	private List<PointDTO> points;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id", nullable = false)
	private Business business; // FK a Business
}
