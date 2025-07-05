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

	private Long userId; // ← the user_id extends token

	private String name;
	private String description;
	private String address;

	@Lob
	@Column(name = "backdrop", columnDefinition = "LONGBLOB")
	private byte[] backdrop;

	private Boolean isActive = true;

	@Temporal(TemporalType.TIMESTAMP)
	private String createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private String updatedAt;
}
