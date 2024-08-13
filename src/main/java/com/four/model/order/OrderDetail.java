package com.four.model.order;

import com.four.model.products.Product;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "orderdetail")
@Getter @Setter
@NoArgsConstructor
public class OrderDetail {
	
	@EmbeddedId
	private OrderDetailID orderDetailID;
	private Integer quantity;
	private Integer unitPrice;
	@ManyToOne
	@MapsId("orderID")
	private Order order;
	@ManyToOne
	@MapsId("productID")
	private Product product;
	
	public OrderDetail(OrderDetailID orderDetailID, Integer quantity, Integer unitPrice) {
		this.orderDetailID = orderDetailID;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}
}