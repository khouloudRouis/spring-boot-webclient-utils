package com.webclient.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webclient.service.GenericWebClientService;

import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ExampleController {

	private final GenericWebClientService webClientService;

	public ExampleController(GenericWebClientService webClientService) {
		this.webClientService = webClientService;
	}

	@GetMapping("/get/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> testGetCall(@PathVariable String id) {
		return webClientService.get("/posts/" + id, null, Map.class).map(ResponseEntity::ok);
	}

	@PostMapping("/post")
	public Mono<ResponseEntity<Map<String, Object>>> testPostCall(@RequestBody Map<String, String> payload) {
		return webClientService.post("/posts", payload, Map.class).map(ResponseEntity::ok);
	}


}