package com.example.demo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

	private String city;
	private String street;
	private String zipcode;

	public static Address of(String city, String street, String zipcode) {
		return new Address(city, street, zipcode);
	}

}
