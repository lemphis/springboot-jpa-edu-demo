package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders(Pageable pageable) {
		return ResponseEntity.ok(orderService.findAll(pageable).getContent());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.findById(id));
	}

	@PostMapping
	public ResponseEntity<Order> getAllOrders(@RequestBody Order order) throws URISyntaxException {
		Order newOrder = orderService.insert(order);
		return ResponseEntity.created(new URI("/order/" + newOrder.getId())).body(newOrder);
	}

}
