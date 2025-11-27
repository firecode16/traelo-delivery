package com.traelo.delivery.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bucket Entity representing a storage bucket in the system.
 */
@Entity
@Table(name = "bucket_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bucket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "object_id", unique = true)
	private String objectId;

	@Column(name = "name")
	private String name;

	@Column(name = "mime_type")
	private String mimeType;

	@Column(name = "size")
	private Long size;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id", nullable = false)
	private Business business;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sector_id", nullable = false)
	private Sector sector;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToOne(mappedBy = "bucket", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private BucketImage bucketImage;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
