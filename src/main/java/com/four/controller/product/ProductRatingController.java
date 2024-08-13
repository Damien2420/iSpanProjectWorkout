package com.four.controller.product;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.four.model.memberAdm.MemberBean;
import com.four.model.order.Order;
import com.four.model.products.Product;
import com.four.model.products.ProductReview;
import com.four.service.memberAdm.MemberService;
import com.four.service.order.OrderService;
import com.four.service.products.ProductReviewService;
import com.four.service.products.ProductService;

@RestController
public class ProductRatingController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductReviewService reviewService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ProductService productService;
	
	@PostMapping("/rating")
    public ResponseEntity<Void> receiveRatings(@RequestParam("oid") String orderID, @RequestBody List<Map<String, Object>> ratings) {
		
		Order order = orderService.findOrderByOrderID(orderID);
        for (Map<String, Object> rating : ratings) {
            Integer productID = Integer.parseInt(rating.get("id").toString());
            Integer score = (Integer) rating.get("score");
            
            MemberBean member = memberService.findById(order.getMemberID());
            Product product = productService.singleSearchProductWithoutImage(productID);
            ProductReview productReview = new ProductReview(score, null, product, member);
            reviewService.saveReview(productReview); 
        }
        order.setRatingStatus(true);
        orderService.updateRatingStatus(order);
        return ResponseEntity.ok().build();
    }
}
