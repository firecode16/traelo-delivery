package com.traelo.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Bucket;
import com.traelo.delivery.model.Product;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {
	Optional<Bucket> findByObjectId(String objectId);

	List<Bucket> findByProductProductIdAndBusinessBusinessIdAndSectorSectorId(Long productId, Long businessId, Long sectorId);

	Optional<Bucket> findByProduct(Product product);
}
