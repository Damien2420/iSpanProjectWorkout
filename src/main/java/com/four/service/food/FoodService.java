package com.four.service.food;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.four.model.food.Food;
import com.four.model.food.FoodRepository;

@Service
//@RequestMapping("/food")
public class FoodService {
	
	@Autowired
	private FoodRepository foodRepo;
	
	// 取整張食品總表 - 有分頁
	public Page<Food> getPaginatedFoodsByCategories(List<Integer> categories, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "foodID");
        return foodRepo.findByCategoryIn(categories, pageable);
    }
		
	//新增 - 處理 新增食物表單 提交到SQL & 自動計算熱量
	public Food save(Food food) {
		float totalCaloriesPer100g = food.calculateTotalCaloriesPer100g();
	    food.setTotalCaloriesPer100g(totalCaloriesPer100g);
		return foodRepo.save(food);
	}
	
	
	//模糊查詢 + 分頁
	public  Page<Food> findByFoodName(String foodName,Integer pageNumber,Integer pageSize){
		Pageable pageable =PageRequest.of(pageNumber,pageSize,Sort.Direction.ASC,"foodID");
		return foodRepo.findByNameContaining(foodName,pageable);
	}
	
	public Food findById(Integer foodMenuID) {
	    Optional<Food> food = foodRepo.findById(foodMenuID);
	    return food.orElse(null);
	}
}
