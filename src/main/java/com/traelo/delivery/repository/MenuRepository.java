package com.traelo.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	List<Menu> findByBusinessId(Long businessId);

	Optional<Menu> findByMenuId(Long menuId);

	void deleteByMenuId(Long menuId);
}
