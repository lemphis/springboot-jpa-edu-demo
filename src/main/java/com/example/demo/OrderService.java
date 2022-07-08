package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public Page<Order> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	public Order findById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new NotExistException("주문 번호에 해당하는 주문이 없습니다."));
	}

	public Order insert(Order order) {
		return orderRepository.save(order);
	}

}
