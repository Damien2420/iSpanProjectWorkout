package com.four.service.order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.order.Order;
import com.four.model.order.OrderRepository;
import com.four.model.order.OrderStatus;
import com.four.model.order.ShippingMethods;
import com.four.model.products.CartDTO;
import com.four.utils.OrderSpecification;
import com.four.utils.OrderStatusStrategyUtils;

@Service
@Transactional
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderStatusService statusService;
	@Autowired
	private ShippingMethodService methodService; 
	
	//照成立日期查全部
	public Page<Order> findAllOrdersByOrderDate(Integer pageNumber){
		Pageable pageSetting = PageRequest.of(pageNumber - 1, 5, Sort.Direction.DESC, "orderDate");
		return orderRepository.findAll(pageSetting);
	}
	
	//狀態類別查詢
	public Page<Order> findOrdersByStatus(Integer pageNumber, Integer statusID, String sortColumnName){
		Pageable pageSetting = PageRequest.of(pageNumber - 1, 5, Sort.Direction.DESC, sortColumnName);
		return orderRepository.findAllByOrderStatus(statusID, pageSetting);
	}
	
	
	public Order findOrderByOrderID(String orderID) {
		Optional<Order> result = orderRepository.findById(orderID);
		Order searchedOrder = result.isPresent() ? result.get() : null;
		return searchedOrder;
	}
	
	public Order insertOrder(Order order) {
		return orderRepository.save(order);
	}
	
	//update & cancel
	public Order changeOrderStatus(Order newOrderInfo) {
		Integer statusID = newOrderInfo.getOrderStatus().getStatusID();
		LocalDateTime updateTime = LocalDateTime.now();
		switch (statusID) {
		case 1:
			OrderStatusStrategyUtils.createdStrategy(newOrderInfo);
			break;
		case 2:
			OrderStatusStrategyUtils.paidStrategy(newOrderInfo, updateTime);
			break;
		case 3:
			OrderStatusStrategyUtils.shippedStrategy(newOrderInfo, updateTime);
			break;
		case 4:
			OrderStatusStrategyUtils.completedStrategy(newOrderInfo, updateTime);
			break;
		case 5:
			OrderStatusStrategyUtils.canceledStrategy(newOrderInfo, updateTime);
			break;
		}
		return orderRepository.save(newOrderInfo);
	}
	
	//成立訂單
	public Order createOrder(String sessionID, Integer price, Integer methodID, String address, List<CartDTO> cart) {
		LocalDateTime now = LocalDateTime.now();
		String orderID = createOrderID(now, sessionID);
		Integer memberNo = cart.get(0).getMemberNo();
		
		OrderStatus orderStatus = statusService.findStatusByID(1);
		ShippingMethods method = methodService.findMethodsByID(methodID);
		return new Order(orderID, memberNo, price , orderStatus, method, address);
	}
	
	//年 月 日 秒 4碼sessionID
    private String createOrderID(LocalDateTime time, String sessionID) {
    	String year = String.valueOf(time.getYear()).substring(2);
        String month = String.format("%02d", time.getMonthValue());
        String day = String.format("%02d", time.getDayOfMonth());
        String second = String.format("%02d", time.getSecond());
        
        Random random = new Random();
        int startIndex = random.nextInt(sessionID.length() - 4 + 1);
        String sid = sessionID.substring(startIndex, startIndex + 4);
        return year + month + day + second + sid;
    }
    
    //使用Specification模糊查詢
  	public Page<Order> fuzzySearch(String column, String keyword, Integer pageNumber){
  		Specification<Order> queryCondition = null;
  		Pageable pageSetting = null;
  		if (column.contains("Date")) {
  			
  			pageSetting = PageRequest.of(pageNumber - 1, 5, Sort.Direction.DESC, column);
			queryCondition = OrderSpecification.dateEquals(column, keyword);
			
		}else if (column.contains("memberID")) {
			
			pageSetting = PageRequest.of(pageNumber - 1, 5, Sort.Direction.DESC, "orderDate");
			return orderRepository.findByMemberID(Integer.parseInt(keyword), pageSetting);
			
		}else {			
			
			pageSetting = PageRequest.of(pageNumber - 1, 5, Sort.Direction.ASC, column);
			queryCondition = OrderSpecification.fuzzyCondition(keyword);
			
		}
  		return orderRepository.findAll(queryCondition, pageSetting);
  	}
  	
  	public List<Order> findOrdersByMemberID(int memberID) {
  		return orderRepository.findByMemberIDOrderByOrderDateDesc(memberID);
  	}
  	
  	public Order updateRatingStatus(Order updatedOrder) {
  		return orderRepository.save(updatedOrder);
  	}
  	
  	public Map<Integer, Long> findOrderCountByMonth() {
  		List<Object[]> statisticList = orderRepository.findOrderCountByMonth();
  		HashMap<Integer, Long> dataMap = statisticList.stream().collect(Collectors.toMap(
            result -> (Integer) result[0],
            result -> (Long) result[1],
            (existing, replacement) -> existing,
            HashMap::new
  	    ));
  		return dataMap;
  	}
}
