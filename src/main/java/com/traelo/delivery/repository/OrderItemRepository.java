package com.traelo.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Order;
import com.traelo.delivery.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findByOrder(Order order);
}
