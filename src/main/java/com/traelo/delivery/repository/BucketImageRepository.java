package com.traelo.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.BucketImage;

@Repository
public interface BucketImageRepository extends JpaRepository<BucketImage, Long> {
}
