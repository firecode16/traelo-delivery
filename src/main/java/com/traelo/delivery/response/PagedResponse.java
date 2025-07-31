package com.traelo.delivery.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
	private List<T> content;
	private int page;
	private int size;
	private int totalPages;
	private long totalElements;
	private boolean last;
}
