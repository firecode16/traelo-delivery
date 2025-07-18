package com.traelo.delivery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Order;
import com.traelo.delivery.model.OrderItem;
import com.traelo.delivery.repository.OrderItemRepository;
import com.traelo.delivery.repository.OrderRepository;
import com.traelo.delivery.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Transactional(readOnly = true)
	@Override
	public List<OrderItem> getItemsByOrderId(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
		return orderItemRepository.findByOrder(order);
	}
}