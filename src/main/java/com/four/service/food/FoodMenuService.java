package com.four.service.food;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.food.Food;
import com.four.model.food.FoodMenu;
import com.four.model.food.FoodMenuDTO;
import com.four.model.food.FoodMenuDetailDTO;
import com.four.model.food.FoodMenuDetailRepository;
import com.four.model.food.FoodMenuRepository;
import com.four.model.food.FoodRepository;

@Service
@Transactional
public class FoodMenuService {

	@Autowired
	private FoodMenuRepository menuRepo;

	@Autowired
	private FoodRepository foodRepo;

	@Autowired
	private FoodMenuDetailRepository menuDetailRepo;

	// 查整張菜單
	public List<FoodMenu> findAllMenus() {
		return menuRepo.findAll();
	}

	// 查整張食品
	public List<Food> findAllFoods() {
		return foodRepo.findAll();
	}

	// 保存菜單 Prw
	public void foodMenuSave(FoodMenu foodMenu) {
		menuRepo.save(foodMenu);
	}

	// 查會員飲食紀錄
	public List<FoodMenu> findMenusByMemberID(Integer memNo) {
		return menuRepo.findByMemberMemNo(memNo);
	}
	
//	public List<FoodMenuDTO> findMenusByFoodMenuDTO(Integer memNo) {
//		return convertToDTO(menuRepo.findByMemberMemNo(memNo));
//		
//	}
//	
//	// 查詢ByTime
//	public List<FoodMenu> findMenusByCreateTime(Date createTime) {
//        return menuRepo.findByCreateTime(createTime);
//    }
//	
//	private List<FoodMenuDTO> convertToDTO(List<FoodMenu> foodMenus) {
//	    return foodMenus.stream()
//	            .map(foodMenu -> {
//	                FoodMenuDTO dto = new FoodMenuDTO();
//	                dto.setFoodMenuID(foodMenu.getFoodMenuID());
//	                dto.setMemberName(foodMenu.getMember().getMemName());
//	                dto.setTotalCal(foodMenu.getTotalCal());
//	                dto.setCreateTime(foodMenu.getCreateTime());
//
//	                List<FoodMenuDetailDTO> detailDTOs = foodMenu.getFoodMenuDetails().stream()
//	                        .map(detail -> new FoodMenuDetailDTO(
//	                                detail.getFoodMenu().getMember(),
//	                                detail.getFood().getFoodID(),
//	                                detail.getFoodWeight(),
//	                                detail.getCalories()))
//	                        .collect(Collectors.toList());
//
//	                dto.setFoodMenuDetails(detailDTOs);
//	                System.out.println(detailDTOs);
//	                return dto;
//	            })
//	            .collect(Collectors.toList());
//	}
	
}
