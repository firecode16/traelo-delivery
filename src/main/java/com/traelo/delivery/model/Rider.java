package com.traelo.delivery.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "riders")
public class Rider {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long riderId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	private String vehicleType;
	private String licensePlate;
	private String phone;

	@Column(precision = 3, scale = 2)
	private BigDecimal ratingAvg = BigDecimal.valueOf(5.0);

	private Boolean isActive = true;

	@Temporal(TemporalType.TIMESTAMP)
	private String createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private String updatedAt;
}
