package com.traelo.delivery.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequestDTO {
	private Long customerId;
	private Long businessId;
	private String address;
	private String notes;
	private BigDecimal total;
	private List<ItemDTO> items;

	public static class ItemDTO {
		private Long menuId;
		private Integer quantity;
		private BigDecimal unitPrice;

		public Long getMenuId() {
			return menuId;
		}

		public void setMenuId(Long menuId) {
			this.menuId = menuId;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public BigDecimal getUnitPrice() {
			return unitPrice;
		}

		public void setUnitPrice(BigDecimal unitPrice) {
			this.unitPrice = unitPrice;
		}
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}
}