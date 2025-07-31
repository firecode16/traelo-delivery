package com.traelo.delivery.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
	private Long userId;
	private String fullName;
	private String email;
	private String phone;
}
