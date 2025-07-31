package com.traelo.delivery.controller;

import static com.traelo.delivery.util.Util.getImageMimeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Menu;
import com.traelo.delivery.model.dto.MenuDTO;
import com.traelo.delivery.service.MenuService;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	@PostMapping("/create")
	public ResponseEntity<?> createMenu(@RequestParam Long menuId, @RequestParam Long businessId, @RequestParam String name, @RequestParam String description, @RequestParam String category, @RequestParam BigDecimal price, @RequestParam("imagen") MultipartFile fileImagen) throws Exception {
		Menu menu = new Menu();
		menu.setMenuId(menuId);
		menu.setBusinessId(businessId);
		menu.setName(name);
		menu.setDescription(description);
		menu.setCategory(category);
		menu.setPrice(price);
		menu.setIsActive(true);
		menu.setCreatedAt(LocalDateTime.now().toString());

		if (fileImagen != null && !fileImagen.isEmpty()) {
			menu.setImage(fileImagen.getBytes());
		}

		return ResponseEntity.ok(menuService.createMenu(menu));
	}

	@GetMapping("/getMenusByBusiness/{businessId}")
	public ResponseEntity<List<MenuDTO>> getMenusByBusiness(@PathVariable Long businessId) {
		return ResponseEntity.ok(menuService.getMenusByBusinessId(businessId));
	}

	@GetMapping("/getMenuById/{menuId}")
	public ResponseEntity<MenuDTO> getMenuById(@PathVariable Long menuId) {
		MenuDTO menu = menuService.getMenuById(menuId);
		
		if (menu == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(menu);
	}

	@GetMapping("/getImage/{menuId}")
	public ResponseEntity<byte[]> getMenuImage(@PathVariable Long menuId) {
		byte[] imageBytes = menuService.getMenuImage(menuId);

		// get type MIME
		String mimeType = getImageMimeType(imageBytes);
		if (mimeType == null) {
			return ResponseEntity.badRequest().body(null);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(mimeType));
		return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	}

	@PutMapping("/update/{menuId}")
	public ResponseEntity<?> updateMenu(@PathVariable Long menuId, @RequestParam String name, @RequestParam String description, @RequestParam String category, @RequestParam BigDecimal price, @RequestParam("imagen") MultipartFile fileImagen) throws Exception {
		Menu updated = new Menu();
		updated.setMenuId(menuId);
		updated.setName(name);
		updated.setDescription(description);
		updated.setCategory(category);
		updated.setPrice(price);
		updated.setUpdatedAt(LocalDateTime.now().toString());

		if (fileImagen != null && !fileImagen.isEmpty()) {
			updated.setImage(fileImagen.getBytes());
		}

		return ResponseEntity.ok(menuService.updateMenu(menuId, updated));
	}

	@DeleteMapping("/delete/{menuId}")
	public ResponseEntity<?> deleteMenu(@PathVariable Long menuId) {
		menuService.deleteMenu(menuId);
		return ResponseEntity.ok("Menú eliminado");
	}
}
