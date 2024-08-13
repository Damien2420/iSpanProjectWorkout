package com.four.model.products;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "productimage")
@Getter @Setter
@NoArgsConstructor
public class ProductImage {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageID;
	private String imageFileName;
	private Integer imageSequence;
	@Lob
	private byte[] productImage;
	@ManyToOne
	@JoinColumn(name = "productID")
	private Product product;

	public ProductImage(String imageFileName, Integer imageSequence, byte[] productImage, Product product) {
		this.imageFileName = imageFileName;
		this.imageSequence = imageSequence;
		this.productImage = productImage;
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductImage [imageID=" + imageID + ", imageFileName=" + imageFileName + ", imageSequence="
				+ imageSequence + "]";
	}
	
	
}
