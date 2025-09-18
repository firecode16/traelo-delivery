package com.traelo.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

	@Lob
	@Column(name = "backdrop", columnDefinition = "LONGBLOB")
	private byte[] backdrop;

	private Boolean isActive = true;

	@Column(name = "accept_cash")
	private Boolean acceptCash = true;

	@Column(name = "accept_transfer")
	private Boolean acceptTransfer = false;

	@Column(name = "bank_clabe")
	private String bankClabe;

	@Column(name = "bank_card")
	private String bankCard;

	@Column(name = "pick_up")
	private Boolean pickUp = true;

	@Column(name = "at_home")
	private Boolean atHome = false;

	@Temporal(TemporalType.TIMESTAMP)
	private String createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private String updatedAt;
}
