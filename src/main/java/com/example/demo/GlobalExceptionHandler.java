package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleOrderNotExistException(NotExistException exception) {
		return exception.getMessage();
	}

}
