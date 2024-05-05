package com.feriodev.spring.cloud.app.client.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Item {

	private Product product;
	private Integer count;
	
	public Item(Product product, Integer count) {
		this.product = product;
		this.count = count;
	}
	
	public Double getTotal() {
		return product.getPrice() * this.count.doubleValue();
	}
}
