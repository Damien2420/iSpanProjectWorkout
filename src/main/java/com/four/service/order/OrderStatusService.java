package com.four.service.order;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.order.OrderStatus;
import com.four.model.order.OrderStatusRepository;

@Service
@Transactional
public class OrderStatusService {

	@Autowired
	private OrderStatusRepository statusRepository;
	
	public OrderStatus findStatusByID(Integer id) {
		Optional<OrderStatus> result = statusRepository.findById(id);
		return result.isPresent() ? result.get() : null;
	}
	
	public List<String> findAllStatusName(){
		return statusRepository.findAllStatusName();
	}
}
