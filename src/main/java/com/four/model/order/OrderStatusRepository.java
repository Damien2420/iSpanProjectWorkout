package com.four.model.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
	
	@Query("SELECT os.status FROM OrderStatus os")
	List<String> findAllStatusName();
}
