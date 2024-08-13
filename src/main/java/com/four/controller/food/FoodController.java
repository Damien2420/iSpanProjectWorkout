package com.four.controller.food;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.food.Food;
import com.four.model.food.FoodRepository;
import com.four.service.food.FoodService;

@Controller
@RequestMapping("/admin/food")//根路徑
public class FoodController {
	
	@Autowired
	private FoodService foodService;
	
	@Autowired
	private FoodRepository foodRepo;
	
	// 查整張表 - 有分頁
	@GetMapping("/ShowFood")
	public String showFoodPage(
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size,
			Model model) {
		
		//默認顯示所有類別
		List<Integer> category = Arrays.asList(1, 2);

		Page<Food> foodPage = foodService.getPaginatedFoodsByCategories(category, page, size);
		model.addAttribute("page", foodPage);
		model.addAttribute("category", category);
	    
        return "food/ShowFood";
    }

	// 查詢byName - 模糊查詢 + 分頁
	@GetMapping("/QueryByFoodName")
	public String queryByFoodName(Model model,
			@RequestParam(value = "foodName") String foodName,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size) {
		Page<Food> Page = foodService.findByFoodName(foodName,page,size);
		model.addAttribute("foods",Page.getContent());
		model.addAttribute("page", Page);
		model.addAttribute("param",foodName);
		return "food/QueryByFoodName";
	}
	
	
	// 新增 - 顯示 新增食物表單
	@GetMapping("/InsertFood")
	public String showInsertFoodForm(Model model) {
		model.addAttribute("food", new Food());
		return "food/InsertFood";
	}
	// 新增 - 處理 新增食物表單 提交到SQL
	@PostMapping("/InsertFood")
	public String insertFood(@ModelAttribute("food") Food food,BindingResult result) {
		//檢查
		if (result.hasErrors()) {
            return "food/InsertFood";
        }
		foodService.save(food);
		return "redirect:/admin/food/ShowFood";//回到食品首頁
	}
	
	// 刪除byID
	@GetMapping("/DeleteByFoodID")
	public String deleteByFoodID(@RequestParam("foodID") Integer id,Model model) {
		foodRepo.deleteById(id);
		return "redirect:/admin/food/ShowFood";//回到食品首頁
	}
	
	// 刪除byID2
	@ResponseBody
	@GetMapping("/DeleteByFoodID2")
	public void deleteByFoodID2(@RequestParam("foodID") Integer id) {
		foodRepo.deleteById(id);
	}
	
	// 更新 - 顯示 更新食物表單
	@GetMapping("/UpdateFood")
	public String showUpdateFoodForm(@RequestParam("foodID") Integer id,Model model) {
		
		//判斷是否有值
		Optional<Food> foodOptional = foodRepo.findById(id);
		if (foodOptional.isPresent()) {
	        Food food = foodOptional.get();
	        model.addAttribute("food", food);
	        return "food/UpdateFood";
	    } else {
	    	//找不到就回首頁
	    	return "redirect:/admin/food/ShowFood";
	    }
	}
	// 更新 - 處理 更新食物表單 提交到SQL
	@PostMapping("/UpdateFood")
	public String updateFood(@ModelAttribute("food") Food food) {
		foodService.save(food);
		return "redirect:/admin/food/ShowFood";//回到食品首頁
	}
}
