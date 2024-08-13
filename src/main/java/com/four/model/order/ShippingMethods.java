package com.four.model.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "shippingmethods")
@Getter @Setter
@NoArgsConstructor
public class ShippingMethods {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer shippingMethodID;
	private String shippingMethod;
	private Integer shippingFee;
}
