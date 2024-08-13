package com.four.service.order;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.order.ShippingMethods;
import com.four.model.order.ShippingMethodsRepository;

@Service
@Transactional
public class ShippingMethodService {
	
	@Autowired
	private ShippingMethodsRepository methodsRepository;
	
	public ShippingMethods findMethodsByID(int methodID) {
		Optional<ShippingMethods> optional = methodsRepository.findById(methodID);
		if(!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}
}
