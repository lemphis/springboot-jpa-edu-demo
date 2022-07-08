package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class OrderItemController {

	private final OrderItemService orderItemService;

	@GetMapping("/{id}")
	public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
		return ResponseEntity.ok(orderItemService.findById(id));
	}

}
