package com.webclient.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<String> handleWebClientException(WebClientResponseException ex) {
		log.error("WebClient error: status={} message={}", ex.getStatusCode(), ex.getMessage(), ex);
		String body = ex.getResponseBodyAsString();
		String safe = (body != null && body.length() > 0) ? body : ex.getMessage();
		return ResponseEntity.status(ex.getStatusCode()).body(safe);
	}

	@ExceptionHandler(DecodingException.class)
	public ResponseEntity<String> handleDecodingException(DecodingException ex) {
		log.error("Decoding error while processing response", ex);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to decode response from upstream service");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception e) {
		log.error("Unhandled exception", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
	}

}