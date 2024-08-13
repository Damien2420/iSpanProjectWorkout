package com.four.controller.order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.four.model.memberAdm.MemberBean;
import com.four.model.order.Order;
import com.four.model.order.OrderDetail;
import com.four.model.order.OrderStatus;
import com.four.service.memberAdm.MemberService;
import com.four.service.order.OrderDetailService;
import com.four.service.order.OrderService;
import com.four.service.order.OrderStatusService;
import com.four.utils.CartDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService detailService;
    @Autowired
	private OrderStatusService statusService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PagedResourcesAssembler<Order> pagedResourcesAssembler;
    
    
    //會員中心 - 我的訂單頁面
    @GetMapping("/myOrders")
    public String memberCenterOrders(@RequestParam("id") Integer memberID, Model model) {
    	MemberBean member = memberService.findById(memberID);
    	List<Order> orders = orderService.findOrdersByMemberID(memberID);
    	model.addAttribute("member", member);
    	model.addAttribute("allOrders", orders);
    	return "order/MemberOrder";
    }
    
    //成立訂單
    @SuppressWarnings("unchecked")
    @PostMapping("/order/createOrder")
	public String createOrderDetail(@RequestParam Integer orderTotal,
									@RequestParam Integer method,
									@RequestParam String address, HttpSession session) {
    	List<CartDTO> cart = (List<CartDTO>) session.getAttribute("cart");
    	
        Order order = orderService.createOrder(session.getId(), orderTotal, method, address, cart);
        List<OrderDetail> details = detailService.createDetails(order, cart);
        order.setOrderDetails(details);
        Order savedOrder = orderService.insertOrder(order);
        String orderID = savedOrder.getOrderID();
        
        session.removeAttribute("cart");
        return "redirect:/mall/purchaseSuccess?id=" + orderID;
    }
    
    //訂單列表第一頁
    @GetMapping("/admin/order/all")
    public String getAllOrderFirstPage(Model model) {
    	Page<Order> resultPage = orderService.findAllOrdersByOrderDate(1);
    	model.addAttribute("orders", resultPage);
    	return "order/AllOrders";
    }
    
    //訂單列表依照狀態查詢請求
    @ResponseBody
    @GetMapping("/admin/order/statusSort/{statusID}")
    public PagedModel<EntityModel<Order>> getOrdersMatchStatus(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
    								   						   @PathVariable Integer statusID) throws JsonProcessingException {
    	String[] dateColumn = {"orderDate", "paymentDate", "shippingDate", "completeDate", "cancelDate"};
    	Page<Order> resultPage;
    	if(statusID == 0) {
    		resultPage = orderService.findAllOrdersByOrderDate(pageNumber);
    	}else {
    		resultPage = orderService.findOrdersByStatus(pageNumber, statusID, dateColumn[statusID -1]);
		}
    	return pagedResourcesAssembler.toModel(resultPage);
    }
    
    //單筆訂單
    @GetMapping("/admin/order/detail")
    public String getOrderDetail(@RequestParam("oid") String orderID, Model model) {
    	Order orderFound = orderService.findOrderByOrderID(orderID);
    	List<OrderDetail> orderDetails = orderFound.getOrderDetails();
    	MemberBean member = memberService.findById(orderFound.getMemberID());
    	model.addAttribute("order", orderFound);
    	model.addAttribute("details", orderDetails);
    	model.addAttribute("member", member);
    	return "/order/OrderDetails";
    }
    
    //拿到訂單日期相關資料
    @ResponseBody
    @GetMapping("/admin/order/date")
    public ResponseEntity<Map<String, LocalDateTime>> getDates(@RequestParam String id){
    	Order result = orderService.findOrderByOrderID(id);
    	Map<String, LocalDateTime> dates = new HashMap<>();
    	dates.put("orderDate", result.getOrderDate());
    	dates.put("paymentDate", result.getPaymentDate());    	
    	dates.put("shippingDate", result.getShippingDate());
    	dates.put("completeDate", result.getCompleteDate());
    	dates.put("cancelDate", result.getCancelDate());
    	return ResponseEntity.ok(dates);
    }
    
    //取得狀態列表
    @ResponseBody
    @GetMapping("/admin/order/statusList")
    public ResponseEntity<List<String>> getStatusList(){
    	return ResponseEntity.ok(statusService.findAllStatusName());
    }
    
    //更新訂單狀態
    @ResponseBody
    @PostMapping("/admin/order/update")
    public String updateStatus(@RequestBody Map<String, String> data) throws JsonProcessingException {
    	Order orderFound = orderService.findOrderByOrderID(data.get("orderID"));
    	OrderStatus newStatus = statusService.findStatusByID(Integer.parseInt(data.get("statusID")));
    	orderFound.setOrderStatus(newStatus);
    	Order updatedOrder = orderService.changeOrderStatus(orderFound);
    	String orderJson = objectMapper.writeValueAsString(updatedOrder);
    	return orderJson;
    }
    
    //模糊查詢
    @GetMapping("/admin/order/search")
    public String fuzzySearch(@RequestParam(name = "c") String column,
    						  @RequestParam(name = "q") String keyword,
    						  @RequestParam(name = "p", defaultValue = "1") Integer pageNumber, Model model) {
    	Page<Order> searchResult = orderService.fuzzySearch(column, keyword, pageNumber);
    	model.addAttribute("result", searchResult);
    	model.addAttribute("c", column);
    	return "order/SearchOrder";
    }
    
    //ajax模糊查詢
    @ResponseBody
    @GetMapping("/admin/order/search/a")
    public PagedModel<EntityModel<Order>> fuzzySearchAjax(@RequestParam(name = "c") String column,
    							  						  @RequestParam(name = "q") String keyword,
    							  						  @RequestParam(name = "p", defaultValue = "1") Integer pageNumber, 
    							  						  Model model) {
    	Page<Order> searchResult = orderService.fuzzySearch(column, keyword, pageNumber);
    	return pagedResourcesAssembler.toModel(searchResult);
    }
    
    //綠界科技付款完成
    @GetMapping("/ecpay/success")
    public String handlePaymentResponse(@RequestParam("id") String orderID) {
		Order order = orderService.findOrderByOrderID(orderID);
		OrderStatus paidStatus = statusService.findStatusByID(2);
		order.setOrderStatus(paidStatus);
		orderService.changeOrderStatus(order);
		return "redirect:/mall";
    }
    
    @ResponseBody
    @GetMapping("/api/order-count")
    public ResponseEntity<Map<Integer, Long>> getOrderCountStatistic() {
    	Map<Integer, Long> statisticData = orderService.findOrderCountByMonth();
    	return ResponseEntity.ok(statisticData);
    }
}
