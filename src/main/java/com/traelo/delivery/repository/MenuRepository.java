package com.traelo.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	List<Menu> findByBusiness(Business business);
}
