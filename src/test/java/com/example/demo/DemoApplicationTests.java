package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderItemRepository orderItemRepository;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	OrderController orderController;

	@BeforeEach
	public void setup() {
		List<Order> city = LongStream.range(0, 100)
				.mapToObj((idx) -> Order.builder().id(idx).address(Address.of("city", "street", Long.toString(idx + 10))).build())
				.toList();
		orderRepository.saveAll(city);

		Order order = Order.builder()
				.id(1L)
				.address(Address.of("seoul", "eulji", "100-000"))
				.build();
		OrderItem orderItem = OrderItem.builder()
				.id(1L)
				.price(10_000)
				.order(order)
				.build();
		orderItemRepository.save(orderItem);
	}

	@Test
	@DisplayName("Page별 조회")
	public void getAll() throws Exception {
		mockMvc.perform(get("/order")
						.param("page", "2")
						.param("size", "10")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(10)))
				.andExpect(jsonPath("$[0].id", is(21)))
				.andExpect(jsonPath("$[9].id", is(30)));
	}

	@Test
	@DisplayName("개별 조회 - 정상 Order ID로 조회")
	public void getOrderById() throws Exception {
		mockMvc.perform(get("/order/{id}", 2L)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isMap())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.order_status", is("ORDER")))
				.andExpect(jsonPath("$.address.city", is("city")))
				.andExpect(jsonPath("$.address.street", is("street")))
				.andExpect(jsonPath("$.address.zipcode", is("12")));
	}

	@Test
	@DisplayName("단건 조회 - [OrderItem -> Order]")
	public void getOrderItemById() throws Exception {
		mockMvc.perform(get("/item/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isMap())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.price", is(10_000)))
				.andExpect(jsonPath("$.order.order_status", is("ORDER")))
				.andExpect(jsonPath("$.order.address.city", is("seoul")))
				.andExpect(jsonPath("$.order.address.street", is("eulji")))
				.andExpect(jsonPath("$.order.address.zipcode", is("100-000")));
	}

	@Test
	@Transactional
	@DisplayName("개별 조회 - 존재하지 않는 Order ID로 조회")
	public void getOrderByIdAndError() throws Exception {
		mockMvc.perform(get("/order/{id}", 10_000L)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$", is("주문 번호에 해당하는 주문이 없습니다.")));
	}

	@Test
	@Transactional
	@DisplayName("단건 insert")
	public void insert() throws Exception {
		Order order = Order.builder()
				.address(Address.of("test", "seoul", "104-217"))
				.build();

		mockMvc.perform(post("/order")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$").isMap());
	}

	@Test
	@Transactional
	@DisplayName("handler 예외 테스트")
	public void exceptionTest() {
		assertThrows(NotExistException.class, () -> orderController.getOrderById(1_000_000L));
	}

}
