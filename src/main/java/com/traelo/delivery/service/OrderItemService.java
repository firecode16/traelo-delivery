package com.traelo.delivery.service;

import java.util.List;

import com.traelo.delivery.model.OrderItem;

public interface OrderItemService {
	List<OrderItem> getItemsByOrderId(Long orderId);
}