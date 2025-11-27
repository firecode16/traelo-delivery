package com.traelo.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByProductIdAndBusinessBusinessId(Long productId, Long businessId);

	Optional<Product> findByProductId(Long productId);

	List<Product> findByBusinessBusinessId(Long businessId);

	void deleteByProductId(Long productId);
}
