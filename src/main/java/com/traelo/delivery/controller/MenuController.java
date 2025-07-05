package com.traelo.delivery.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Menu;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.service.MenuService;

@RestController
@CrossOrigin("*")
public class MenuController {

	@Autowired
	private MenuService menuService;

	@Autowired
	private BusinessRepository businessRepository;

	@PostMapping(value = "/menu/create", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createMenu(@RequestParam String name, @RequestParam String description, @RequestParam BigDecimal price, @RequestParam Long businessId, @RequestParam(required = false) MultipartFile image) throws Exception {
		Business business = businessRepository.findById(businessId).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

		Menu menu = new Menu();
		menu.setName(name);
		menu.setDescription(description);
		menu.setPrice(price);
		menu.setIsActive(true);
		menu.setBusiness(business);

		if (image != null && !image.isEmpty()) {
			menu.setImage(image.getBytes());
		}

		return ResponseEntity.ok(menuService.createMenu(menu));
	}

	@GetMapping("/menu/business/{businessId}")
	public ResponseEntity<List<Menu>> getMenusByBusiness(@PathVariable Long businessId) {
		return ResponseEntity.ok(menuService.getMenusByBusinessId(businessId));
	}

	@GetMapping("/menu/getMenuById/{id}")
	public ResponseEntity<Menu> getMenuById(@PathVariable Long id) {
		Optional<Menu> menu = menuService.getMenuById(id);
		return menu.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping(value = "/menu/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateMenu(@PathVariable Long id, @RequestParam String name, @RequestParam String description, @RequestParam BigDecimal price, @RequestParam Boolean isActive, @RequestParam(required = false) MultipartFile image) throws Exception {
		Menu updated = new Menu();
		updated.setName(name);
		updated.setDescription(description);
		updated.setPrice(price);
		updated.setIsActive(isActive);

		if (image != null && !image.isEmpty()) {
			updated.setImage(image.getBytes());
		}

		return ResponseEntity.ok(menuService.updateMenu(id, updated));
	}

	@DeleteMapping("/menu/delete/{id}")
	public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
		menuService.deleteMenu(id);
		return ResponseEntity.ok("Menú eliminado");
	}
}
