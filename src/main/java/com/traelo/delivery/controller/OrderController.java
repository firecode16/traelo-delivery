
package com.traelo.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.traelo.delivery.model.Order;
import com.traelo.delivery.model.dto.OrderRequestDTO;
import com.traelo.delivery.service.OrderItemService;
import com.traelo.delivery.service.OrderService;

@RestController
@CrossOrigin("*")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@PostMapping("/orders/createWithItems")
	public ResponseEntity<?> createOrderWithItems(@RequestBody OrderRequestDTO dto) {
		return ResponseEntity.ok(orderService.createOrderWithItems(dto));
	}

	@GetMapping("/orders/getOrderById/{id}")
	public ResponseEntity<?> getOrderById(@PathVariable Long id) {
		return orderService.getOrderById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/orders/getOrdersByCustomer/{customerId}")
	public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long customerId) {
		return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
	}

	@GetMapping("/orders/getOrdersByBusiness/{businessId}")
	public ResponseEntity<List<Order>> getOrdersByBusiness(@PathVariable Long businessId) {
		return ResponseEntity.ok(orderService.getOrdersByBusinessId(businessId));
	}

	@PutMapping("/orders/update/{id}/status")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
	}

	@GetMapping("/orders/getOrderItems/{orderId}/items")
	public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderItemService.getItemsByOrderId(orderId));
	}
}
