package com.traelo.delivery.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Menu;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.MenuRepository;
import com.traelo.delivery.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private BusinessRepository businessRepository;

	@Transactional
	@Override
	public Menu createMenu(Menu menu) {
		Optional<Business> business = businessRepository.findByBusinessId(menu.getBusinessId());
		if (business.isEmpty()) {
			throw new RuntimeException("Negocio no encontrado");
		}
		return menuRepository.save(menu);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Menu> getMenuById(Long menuId) {
		return menuRepository.findByMenuId(menuId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Menu> getMenusByBusinessId(Long businessId) {
		Optional<Business> business = businessRepository.findByBusinessId(businessId);
		if (business.isEmpty()) {
			throw new RuntimeException("Negocio no encontrado");
		}
		return menuRepository.findByBusinessId(businessId);
	}

	@Transactional
	@Override
	public Menu updateMenu(Long menuId, Menu updatedMenu) {
		Menu menu = menuRepository.findByMenuId(menuId).orElseThrow(() -> new RuntimeException("Menú no encontrado"));

		menu.setMenuId(menuId);
		menu.setName(updatedMenu.getName());
		menu.setDescription(updatedMenu.getDescription());
		menu.setCategory(updatedMenu.getCategory());
		menu.setPrice(updatedMenu.getPrice());
		menu.setUpdatedAt(updatedMenu.getUpdatedAt());

		if (updatedMenu.getImage() != null && updatedMenu.getImage().length > 0) {
			menu.setImage(updatedMenu.getImage());
		}

		return menuRepository.save(menu);
	}

	@Transactional
	@Override
	public void deleteMenu(Long menuId) {
		menuRepository.deleteByMenuId(menuId);
	}
}
