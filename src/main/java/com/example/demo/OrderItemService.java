package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemService {

	private final OrderItemRepository orderItemRepository;

	public OrderItem findById(Long id) {
		return orderItemRepository.findById(id).orElseThrow(() -> new NotExistException("Item 고유 번호에 해당하는 데이터가 없습니다."));
	}

}
