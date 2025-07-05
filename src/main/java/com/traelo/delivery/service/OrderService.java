package com.traelo.delivery.service;

import java.util.List;
import java.util.Optional;

import com.traelo.delivery.model.Order;
import com.traelo.delivery.model.dto.OrderRequestDTO;

public interface OrderService {
	Order createOrderWithItems(OrderRequestDTO orderRequest);

	Optional<Order> getOrderById(Long id);

	List<Order> getOrdersByCustomerId(Long customerId);

	List<Order> getOrdersByBusinessId(Long businessId);

	Order updateOrderStatus(Long orderId, String newStatus);
}