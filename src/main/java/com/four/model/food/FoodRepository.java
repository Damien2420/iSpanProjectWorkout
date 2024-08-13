package com.four.model.food;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodRepository extends JpaRepository<Food, Integer> {
		
	// 查詢ByName - 模糊查詢 + 分頁
	@Query("from Food where foodName like %:foodName%")
	Page<Food> findByNameContaining(@Param(value = "foodName") String foodName,Pageable pageable);
	
	// 查詢ByName - 模糊查詢
	@Query("from Food where foodName like %:foodName%")
	List<Food> findByName(@Param(value = "foodName") String foodName);
	
	// 查詢By分類
	Page<Food> findByCategoryIn(List<Integer> categories, Pageable pageable);
	
}
