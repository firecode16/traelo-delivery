package com.traelo.delivery.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "menus")
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long menuId;
	private String name;
	private String description;

	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal price;

	@Lob
	@Column(name = "image_url", columnDefinition = "LONGBLOB")
	private byte[] image;

	private Boolean isActive = true;

	@Temporal(TemporalType.TIMESTAMP)
	private String createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private String updatedAt;

	@ManyToOne
	@JoinColumn(name = "business_id", nullable = false)
	private Business business;
}