package com.four.model.products;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CartDTO {
	
	private Integer memberNo;
	private Product product;
	private ProductImage image;
	private Integer quantity;

	public CartDTO(Integer memberNo, Product product, ProductImage image, Integer quantity) {
		this.memberNo = memberNo;
		this.product = product;
		this.image = image;
		this.quantity = quantity;
	}
	
}
