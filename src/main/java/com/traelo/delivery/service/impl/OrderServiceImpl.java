package com.traelo.delivery.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Menu;
import com.traelo.delivery.model.Order;
import com.traelo.delivery.model.OrderItem;
import com.traelo.delivery.model.dto.OrderRequestDTO;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.OrderRepository;
import com.traelo.delivery.service.OrderService;
import com.traelo.delivery.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private BusinessRepository businessRepository;

	@Transactional
	@Override
	public Order createOrderWithItems(OrderRequestDTO dto) {
		Business business = businessRepository.findById(dto.getBusinessId()).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

		Order order = new Order();
		order.setCustomerId(dto.getCustomerId());
		order.setBusiness(business);
		order.setAddress(dto.getAddress());
		order.setNotes(dto.getNotes());
		order.setTotal(dto.getTotal());
		order.setStatus(OrderStatus.PENDING);

		List<OrderItem> items = dto.getItems().stream().map(itemDTO -> {
			OrderItem item = new OrderItem();
			item.setOrder(order); // bidirectional relationship
			Menu menu = new Menu(); // we only set IDs to avoid a full fetch
			menu.setId(itemDTO.getMenuId());
			item.setMenu(menu);
			item.setQuantity(itemDTO.getQuantity());
			item.setUnitPrice(itemDTO.getUnitPrice());
			item.setTotalPrice(itemDTO.getUnitPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
			return item;
		}).toList();

		order.setItems(items);
		return orderRepository.save(order); // save everything by cascade
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
		Business business = businessRepository.findById(businessId)
				.orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
		return orderRepository.findByBusiness(business);
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
