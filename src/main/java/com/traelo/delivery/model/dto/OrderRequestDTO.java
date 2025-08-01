package com.traelo.delivery.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.traelo.delivery.util.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {
	private Long orderId;
	private Long businessId;
	private Long customerId;
	private String address;
	private String notes;
	private String deliveryMethod;
	private List<String> jsonOrder;
	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.PENDING;
	private BigDecimal totalPrice;
	private String createdAt;
}