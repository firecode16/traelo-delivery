package com.traelo.delivery.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex) {
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
	}

	private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);

		return new ResponseEntity<>(body, status);
	}
}