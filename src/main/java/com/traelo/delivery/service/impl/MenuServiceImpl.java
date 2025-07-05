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
		return menuRepository.save(menu);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Menu> getMenuById(Long id) {
		return menuRepository.findById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Menu> getMenusByBusinessId(Long businessId) {
		Optional<Business> business = businessRepository.findById(businessId);
		return business.map(menuRepository::findByBusiness).orElse(List.of());
	}

	@Transactional
	@Override
	public Menu updateMenu(Long id, Menu updatedMenu) {
		Menu menu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menú no encontrado"));

		menu.setName(updatedMenu.getName());
		menu.setDescription(updatedMenu.getDescription());
		menu.setPrice(updatedMenu.getPrice());
		menu.setIsActive(updatedMenu.getIsActive());

		if (updatedMenu.getImage() != null && updatedMenu.getImage().length > 0) {
			menu.setImage(updatedMenu.getImage());
		}

		return menuRepository.save(menu);
	}

	@Transactional
	@Override
	public void deleteMenu(Long id) {
		menuRepository.deleteById(id);
	}
}
