package com.four.model.order;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor
public class OrderDetailID implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String orderID;
	private Integer productID;
	
	public OrderDetailID(String orderID, Integer productID) {
		this.orderID = orderID;
		this.productID = productID;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(orderID, productID);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDetailID other = (OrderDetailID) obj;
		return Objects.equals(orderID, other.orderID) && Objects.equals(productID, other.productID);
	}
	
}
