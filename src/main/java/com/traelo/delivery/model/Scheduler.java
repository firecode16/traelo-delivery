package com.traelo.delivery.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "scheduler")
public class Scheduler {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long schedulerId;
	private Long businessId;

	@Enumerated(EnumType.STRING)
	@Column(name = "day_of_week", nullable = true)
	private DayOfWeek dayOfWeek;

	@Column(name = "open_time", nullable = true)
	private LocalTime openTime;

	@Column(name = "close_time", nullable = true)
	private LocalTime closeTime;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;
}
