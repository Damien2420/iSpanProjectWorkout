package com.four.model.products;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "product")
@Getter @Setter
@NoArgsConstructor
public class Product {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productID;
	private String productName;
	private Integer price;
	
	@JsonFormat(timezone = "GMT-8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate addedDate;
	private Integer stock;
	private String productFeatures;
	private Boolean launchStatus = false;
	
	@ManyToOne @JoinColumn(name = "categoryID")
	private ProductCategory productCategory;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductImage> productImage = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductReview> productReviews = new ArrayList<>();

	public Product(String productName, Integer price, LocalDate addedDate, Integer stock,
			String productFeatures, ProductCategory productCategory) {
		this.productName = productName;
		this.price = price;
		this.addedDate = addedDate;
		this.stock = stock;
		this.productFeatures = productFeatures;
		this.productCategory = productCategory;
	}
	
	@PrePersist
	public void onCreate() {
		if(addedDate == null) {
			addedDate = LocalDate.now();
		}
	}
	
	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName=" + productName + ", price=" + price
				+ ", addedDate=" + addedDate + ", stock=" + stock + ", productFeatures=" + productFeatures
				+ ", productCategory=" + productCategory.toString() + "\n, productImage=" + productImage.toString() + "]";
	}

}
