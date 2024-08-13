package com.four.model.order;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "orderstatus")
@Getter @Setter
@NoArgsConstructor
public class OrderStatus {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer statusID;
	private String status;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderStatus")
	private List<Order> orders = new ArrayList<>();

	public OrderStatus(String status) {
		this.status = status;
	}
	
}
