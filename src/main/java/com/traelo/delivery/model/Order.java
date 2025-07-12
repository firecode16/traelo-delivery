package com.traelo.delivery.model;

import java.math.BigDecimal;
import java.util.List;

import com.traelo.delivery.util.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderId;
	private Long customerId;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private OrderStatus status = OrderStatus.PENDING;

	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal total;

	private String address;

	@Column(columnDefinition = "TEXT")
	private String notes;

	@Temporal(TemporalType.TIMESTAMP)
	private String createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private String updatedAt;

	@ManyToOne
	@JoinColumn(name = "business_id", nullable = false)
	private Business business;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> items;
}
