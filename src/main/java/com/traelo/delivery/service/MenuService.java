package com.traelo.delivery.service;

import java.util.List;

import com.traelo.delivery.model.Menu;
import com.traelo.delivery.model.dto.MenuDTO;

public interface MenuService {
	Menu createMenu(Menu menu);

	MenuDTO getMenuById(Long menuId);

	List<MenuDTO> getMenusByBusinessId(Long businessId);

	byte[] getMenuImage(Long menuId);

	Menu updateMenu(Long menuId, Menu menu);

	void deleteMenu(Long menuId);
}
