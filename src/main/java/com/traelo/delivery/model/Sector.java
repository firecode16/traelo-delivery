package com.traelo.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sectors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sector {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private Long sectorId;

	@Column(name = "name", nullable = false, unique = true, length = 50)
	private String name;

	@Column(name = "display_name_product_tab", nullable = false, length = 50)
	private String displayNameProductTab;

	@Column(name = "icon_name", length = 50)
	private String iconName;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;
}
