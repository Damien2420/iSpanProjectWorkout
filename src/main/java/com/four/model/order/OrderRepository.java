package com.four.model.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
	
	@Query("SELECT o FROM Order o WHERE o.orderStatus.statusID = :id")
	Page<Order> findAllByOrderStatus(@Param("id")Integer statusID, Pageable page);
	
	@Query("SELECT o FROM Order o WHERE o.memberID = :id")
	Page<Order> findByMemberID(@Param("id") Integer memberID, Pageable page);
	
	List<Order> findByMemberIDOrderByOrderDateDesc(Integer memberID);
	
	@Query("SELECT MONTH(o.orderDate) AS month, COUNT(o) AS count " +
           "FROM Order o " +
           "GROUP BY MONTH(o.orderDate) " +
           "ORDER BY MONTH(o.orderDate)")
    List<Object[]> findOrderCountByMonth();
}
