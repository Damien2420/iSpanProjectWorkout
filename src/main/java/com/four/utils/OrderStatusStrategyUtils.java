package com.four.utils;

import java.time.LocalDateTime;

import com.four.model.order.Order;

public class OrderStatusStrategyUtils {
	
	public static void createdStrategy(Order order) {
        order.setPaymentDate(null);
        order.setShippingDate(null);
        order.setCompleteDate(null);
    }

    public static void paidStrategy(Order order, LocalDateTime updateTime) {
        order.setPaymentDate(updateTime);
        order.setShippingDate(null);
        order.setCompleteDate(null);
    }

    public static void shippedStrategy(Order order, LocalDateTime updateTime) {
    	if(order.getPaymentDate() == null) {
    		order.setPaymentDate(updateTime);
    	}
        order.setShippingDate(updateTime);
        order.setCompleteDate(null);
    }

    public static void completedStrategy(Order order, LocalDateTime updateTime) {
    	if(order.getPaymentDate() == null) {
    		order.setPaymentDate(updateTime);
    	}
    	if(order.getShippingDate() == null) {
    		order.setShippingDate(updateTime);
    	}
        order.setCompleteDate(updateTime);
    }

    public static void canceledStrategy(Order order, LocalDateTime updateTime) {
        order.setCancelDate(updateTime);
    }
}
