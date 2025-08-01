package com.traelo.delivery.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Order;
import com.traelo.delivery.model.dto.OrderRequestDTO;
import com.traelo.delivery.repository.OrderRepository;
import com.traelo.delivery.service.OrderService;
import com.traelo.delivery.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Transactional
	@Override
	public Order createOrderWithItems(OrderRequestDTO dto) {
		Order order = new Order();
		order.setOrderId(dto.getOrderId());
		order.setCustomerId(dto.getCustomerId());
		order.setBusinessId(dto.getBusinessId());
		order.setStatus(OrderStatus.PENDING);
		order.setAddress(dto.getAddress());
		order.setNotes(dto.getNotes());
		order.setDeliveryMethod(dto.getDeliveryMethod());
		order.setJsonOrder(dto.getJsonOrder());
		order.setTotalPrice(dto.getTotalPrice());
		order.setCreatedAt(dto.getCreatedAt());

		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Order> getOrdersByCustomerId(Long customerId) {
		return orderRepository.findByCustomerId(customerId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Order> getOrdersByBusinessId(Long businessId) {
		return orderRepository.findByBusinessId(businessId);
	}

	@Transactional
	@Override
	public Order updateOrderStatus(Long orderId, String newStatus) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

		try {
			OrderStatus status = OrderStatus.valueOf(newStatus.toUpperCase());
			order.setStatus(status);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException("Estado de pedido inválido");
		}

		return orderRepository.save(order);
	}
}
