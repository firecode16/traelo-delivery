package com.traelo.delivery.model;

import java.math.BigDecimal;
import java.util.List;

import com.traelo.delivery.util.JsonOrderConverter;
import com.traelo.delivery.util.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderId;
	private Long customerId;
	private Long businessId;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private OrderStatus status = OrderStatus.PENDING;

	private String address;

	@Column(columnDefinition = "TEXT")
	private String notes;
	private String deliveryMethod;

	@Column(columnDefinition = "json")
	@Convert(converter = JsonOrderConverter.class)
	private List<String> jsonOrder;

	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal totalPrice;

	@Temporal(TemporalType.TIMESTAMP)
	private String createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private String updatedAt;
}
