package com.feriodev.spring.cloud.app.client.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3472962890720017154L;

	private String id;

	private String name;

	private Double price;

	private Date createAt;

	private Integer port;

	public Product(String name, Double price) {
		super();
		this.name = name;
		this.price = price;
	}
}
