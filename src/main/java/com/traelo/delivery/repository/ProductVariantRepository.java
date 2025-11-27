package com.traelo.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.ProductVariant;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
	@Query("SELECT pv FROM ProductVariant pv WHERE pv.product.productId = :productId")
	List<ProductVariant> findByProductProductId(@Param("productId") Long productId);

	@Modifying
	@Query("DELETE FROM ProductVariant pv WHERE pv.product.productId = :productId")
	void deleteByProductProductId(@Param("productId") Long productId);

	void deleteByProductId(Long productId);
}
