package com.four.model.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
public class Order {
	
	@Id
	private String orderID;
	private Integer memberID;
	private Integer totalPrice;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime paymentDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime shippingDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime completeDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cancelDate;
	
	@ManyToOne
	@JoinColumn(name = "statusID")
	private OrderStatus orderStatus;
	
	@ManyToOne
	@JoinColumn(name = "shippingMethodID")
	private ShippingMethods shippingMethods;
	private String shippingAddress;
	private Boolean ratingStatus = false;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL , mappedBy = "order")
	private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();	
	
	public Order(String orderID, Integer memberID, Integer totalPrice, OrderStatus orderStatus,
			ShippingMethods shippingMethods, String shippingAddress) {
		this.orderID = orderID;
		this.memberID = memberID;
		this.totalPrice = totalPrice;
		this.orderStatus = orderStatus;
		this.shippingMethods = shippingMethods;
		this.shippingAddress = shippingAddress;
	}
	
	@PrePersist
	public void createOrderTime() {
		if(orderDate == null) {
			orderDate = LocalDateTime.now();
		}
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", memberID=" + memberID + ", totalPrice=" + totalPrice + ", orderDate="
				+ orderDate + ", shippingDate=" + shippingDate + ", completeDate=" + completeDate + ", cancelDate="
				+ cancelDate + "]";
	}
}
