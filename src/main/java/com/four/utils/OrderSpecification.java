package com.four.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import com.four.model.order.Order;

public class OrderSpecification {
	
	public static Specification<Order> fuzzyCondition(String keyword){
		return (root, query, builder) -> { return builder.like(root.get("orderID"), "%" + keyword + "%"); };
	}
	
	public static Specification<Order> dateEquals(String column, String date) {
	    return (root, query, builder) -> {
	        LocalDateTime startDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN);
	        LocalDateTime endDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX);
	        return builder.between(root.get(column), startDate, endDate);
	    };
	}
}
