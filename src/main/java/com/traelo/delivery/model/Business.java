package com.traelo.delivery.model;

import com.traelo.delivery.enums.BusinessStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "businesses")
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private Long businessId;
	private Long userId; // ← the user_id extends token

	private String fullName;
	private String description;

	private String address;
	private Double latitude;
	private Double longitude;

	@Lob
	@Column(name = "backdrop", columnDefinition = "LONGBLOB")
	private byte[] backdrop;

	private Boolean isActive = true;
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private BusinessStatus status = BusinessStatus.ACTIVE;

	@Column(name = "accept_cash")
	private Boolean acceptCash = true;

	@Column(name = "accept_transfer")
	private Boolean acceptTransfer = false;

	@Column(name = "bank_clabe")
	private String bankClabe;

	@Column(name = "bank_card")
	private String bankCard;

	@Temporal(TemporalType.TIMESTAMP)
	private String createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private String updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sector_id") // FK to Sector
	private Sector sector;
}
