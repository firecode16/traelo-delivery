package com.traelo.delivery.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Menu;
import com.traelo.delivery.model.dto.MenuDTO;
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
	public MenuDTO getMenuById(Long menuId) {
		Menu menu = menuRepository.findByMenuId(menuId).orElse(null);
		
		if (menu == null) {
			return null;
		}
		
		return new MenuDTO(menu.getMenuId(), menu.getBusinessId(), menu.getName(), menu.getDescription(), menu.getCategory(), menu.getPrice(), menu.getIsActive());
	}

	@Transactional(readOnly = true)
	@Override
	public List<MenuDTO> getMenusByBusinessId(Long businessId) {
		Optional<Business> business = businessRepository.findByBusinessId(businessId);
		if (business.isEmpty()) {
			throw new RuntimeException("Negocio no encontrado");
		}
		return menuRepository.findByBusinessId(businessId).stream().map(m -> new MenuDTO(m.getMenuId(), m.getBusinessId(), m.getName(), m.getDescription(), m.getCategory(), m.getPrice(), m.getIsActive())).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public byte[] getMenuImage(Long menuId) {
		Optional<Menu> optMenu = menuRepository.findByMenuId(menuId);

		if (optMenu.isEmpty()) {
			throw new RuntimeException("Menu no encontrado");
		}

		byte[] imageBytes = optMenu.get().getImage();

		if (imageBytes == null || imageBytes.length == 0) {
			throw new RuntimeException("El menu no cuenta con una imagen");
		}

		return imageBytes;
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
