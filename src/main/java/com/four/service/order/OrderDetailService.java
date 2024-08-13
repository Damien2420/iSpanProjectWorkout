package com.four.service.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.order.Order;
import com.four.model.order.OrderDetail;
import com.four.model.order.OrderDetailID;
import com.four.model.products.Product;
import com.four.utils.CartDTO;

@Service
@Transactional
public class OrderDetailService {
	
	public List<OrderDetail> createDetails(Order order, List<CartDTO> cart){
		List<OrderDetail> details = new ArrayList<>();
        for (CartDTO cartItem : cart) {
        	
        	Integer quantity = cartItem.getQuantity();
        	Integer productPrice = cartItem.getProduct().getPrice();
        	Integer total = quantity * productPrice;
        	
			OrderDetail itemDetail = new OrderDetail();
			Product product = cartItem.getProduct();
			OrderDetailID ids = new OrderDetailID(order.getOrderID(), product.getProductID());
			itemDetail.setOrderDetailID(ids);
			itemDetail.setQuantity(cartItem.getQuantity());
			itemDetail.setUnitPrice(total);
			itemDetail.setOrder(order);
			itemDetail.setProduct(product);
			details.add(itemDetail);
		}
        return details;
	}
}
