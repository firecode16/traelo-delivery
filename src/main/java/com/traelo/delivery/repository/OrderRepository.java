package com.traelo.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCustomerId(Long customerId);

	List<Order> findByBusiness(Business business);
}