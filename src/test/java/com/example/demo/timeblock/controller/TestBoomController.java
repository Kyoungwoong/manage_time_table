package com.example.demo.timeblock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestBoomController {

	@GetMapping("/api/test/boom")
	String boom() {
		throw new RuntimeException("boom");
	}
}
