package com.traelo.delivery.service;

import java.util.List;
import java.util.Optional;

import com.traelo.delivery.model.Menu;

public interface MenuService {
	Menu createMenu(Menu menu);

	Optional<Menu> getMenuById(Long id);

	List<Menu> getMenusByBusinessId(Long businessId);

	Menu updateMenu(Long id, Menu menu);

	void deleteMenu(Long id);
}
