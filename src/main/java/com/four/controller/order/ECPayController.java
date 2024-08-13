package com.four.controller.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.four.model.order.Order;
import com.four.model.order.OrderDetail;
import com.four.service.order.OrderService;
import com.four.utils.ecpay.payment.integration.AllInOne;
import com.four.utils.ecpay.payment.integration.domain.AioCheckOutALL;

@RestController
public class ECPayController {
	
	@Autowired
	private AllInOne allInOne;
	@Autowired
	private AioCheckOutALL aioCheckOutALL;
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/ecpay/payment")
	public String createECPayOrder(@RequestParam("id") String orderID) {
		Order order = orderService.findOrderByOrderID(orderID);

		StringBuffer itemName = new StringBuffer();
		for (OrderDetail item : order.getOrderDetails()) {
			itemName.append(item.getProduct().getProductName() + "X" + item.getQuantity() + "#");
		}
		
		if (order.getShippingMethods().getShippingMethodID() == 1) {
			itemName.append("宅配運費X1#");			
		}
		
        LocalDateTime date = order.getOrderDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String orderDate = date.format(formatter);
		
        AllInOne all = new AllInOne("");
        
        AioCheckOutALL aio = new AioCheckOutALL();
		aioCheckOutALL.setMerchantTradeNo(orderID);
		aioCheckOutALL.setMerchantTradeDate(orderDate);
		aioCheckOutALL.setTotalAmount(order.getTotalPrice().toString());
		aioCheckOutALL.setTradeDesc("GYMNITY商城商品");
		aioCheckOutALL.setItemName(itemName.toString());
		aioCheckOutALL.setClientBackURL("http://localhost:8081/workout/ecpay/success?id=" + orderID);
		aioCheckOutALL.setReturnURL("http://localhost:8081/workout/ecpay/success?id=" + orderID);
		aioCheckOutALL.setNeedExtraPaidInfo("N");
		
		String form = allInOne.aioCheckOut(aioCheckOutALL, null);
		System.out.println(form);
		return form;
	}
}
