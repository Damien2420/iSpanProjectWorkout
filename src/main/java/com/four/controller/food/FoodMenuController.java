package com.four.controller.food;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.food.Food;
import com.four.model.food.FoodMenu;
import com.four.model.food.FoodMenuDetail;
import com.four.model.food.FoodMenuDetailDTO;
import com.four.model.memberAdm.MemberBean;
import com.four.service.food.FoodMenuService;
import com.four.service.food.FoodService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class FoodMenuController {

	@Autowired
	private FoodMenuService menuService;

	@Autowired
	private FoodService foodService;

	// 進 熱量計算機
	@GetMapping("/foodMenu/MenuCre")
	public String getMenuCre(Model model) {
		List<Food> foods = menuService.findAllFoods();
		model.addAttribute("foods", foods);
		return "foodMenu/MenuCre";
	}

	// 取得所有食物
	@ResponseBody
	@GetMapping("/foodMenu/api/findAllMenus")
	public List<FoodMenu> findAllMenus() {
		return menuService.findAllMenus();
	}
	
	
	// 取得所有菜單
	@ResponseBody
	@GetMapping("/foodMenu/api/getAllFoodForMenu")
	public List<Food> getAllFoodForMenu() {
		return menuService.findAllFoods();
	}
	
	// 藉會員ID找本人菜單
	@ResponseBody
	@GetMapping("/foodMenu/api/findMenusByMemberID")
	public List<FoodMenu> findMenusByMemberID(HttpSession httpSession) {
		MemberBean user = (MemberBean) httpSession.getAttribute("user");
		return menuService.findMenusByMemberID(user.getMemNo());
	}
	
//	@ResponseBody
//	@GetMapping("/api/findMenusByFoodMenuDTO")
//	public List<FoodMenuDTO> findMenusByFoodMenuDTO(HttpSession httpSession) {
//		MemberBean user = (MemberBean) httpSession.getAttribute("user");
//		return menuService.findMenusByFoodMenuDTO(user.getMemNo());
//	}

	// 設定清單用類似購物車裝起來 ->按下增加食品做一次
	@ResponseBody
	@PostMapping("/foodMenu/api/setFoodMenuDetailSession")
	@SuppressWarnings("unchecked")
	public void setFoodMenuDetailSession(@RequestBody FoodMenuDetailDTO foodMenuDetailDTO, HttpSession httpSession) {
		List<FoodMenuDetailDTO> cart = (List<FoodMenuDetailDTO>) httpSession.getAttribute("fcart");
		if (cart == null) {
			cart = new ArrayList<>();
		}
		cart.add(foodMenuDetailDTO);
		httpSession.setAttribute("fcart", cart);
		System.out.println("新增" + cart);
	}

	// 按下生成菜單 -> 抓cart 所有資料 放到sql
	@ResponseBody
	@GetMapping("/foodMenu/api/setFoodMenu")
	@SuppressWarnings("unchecked")
	public void setFoodMenu(@RequestParam float totalCal, HttpSession httpSession) {
		List<FoodMenuDetailDTO> cart = (List<FoodMenuDetailDTO>) httpSession.getAttribute("fcart");
		if (cart != null && !cart.isEmpty()) {
			FoodMenu foodMenu = new FoodMenu();
			List<FoodMenuDetail> foodMenuDetails = cart.stream().map(dto -> {
				FoodMenuDetail detail = convertToEntity(dto);
				detail.setFoodMenu(foodMenu); // 設置正確的關聯
				return detail;
			}).collect(Collectors.toList());
			foodMenu.setFoodMenuDetails(foodMenuDetails);
			// 熱量算到小數點後一位，不進行四捨五入
			float truncatedTotalCal = (float) Math.floor(totalCal * 10) / 10;
			foodMenu.setTotalCal(truncatedTotalCal);

			// 從session取會員
			MemberBean user = (MemberBean) httpSession.getAttribute("user");
			if (user != null) {
				foodMenu.setMember(user);
			} else {
				throw new IllegalStateException("No member found in session");
			}

			foodMenu.setCreateTime(new Timestamp(System.currentTimeMillis()));
			menuService.foodMenuSave(foodMenu);

			// 保存完菜單，清空cart
			httpSession.removeAttribute("fcart");
		}
	}

	// 把牛變成鱒魚
	private FoodMenuDetail convertToEntity(FoodMenuDetailDTO foodMenuDetailDTO) {
		FoodMenuDetail foodMenuDetail = new FoodMenuDetail();
		foodMenuDetail.setFoodWeight(foodMenuDetailDTO.getFoodWeight());
		foodMenuDetail.setCalories(foodMenuDetailDTO.getCalories());
		foodMenuDetail.setFood(foodService.findById(foodMenuDetailDTO.getFoodID()));
		return foodMenuDetail;
	}

	// 購物車清除某筆 -> 你按下刪除按鍵會做這件事 還沒做這件事
	@ResponseBody
	@PostMapping("/foodMenu/api/removeFoodFromCart")
	@SuppressWarnings("unchecked")
	public void removeFoodFromCart(@RequestBody String foodID, HttpSession httpSession) {
		List<FoodMenuDetailDTO> cart = (List<FoodMenuDetailDTO>) httpSession.getAttribute("fcart");
		if (cart != null) {
			// 使用迭代器刪除元素
			Iterator<FoodMenuDetailDTO> iterator = cart.iterator();
			while (iterator.hasNext()) {
				FoodMenuDetailDTO item = iterator.next();
				if (item.getFoodID().equals(foodID)) {
					iterator.remove();
					break;
				}
			}
			httpSession.setAttribute("fcart", cart);
			System.out.println("刪除後" + cart);
		}
	}

	// 進 使用者飲食管理 - 後台
	@GetMapping("/admin/foodMenu/allMenus")
	public String getAllMenus(Model model) {
		List<FoodMenu> allMenus = menuService.findAllMenus();
		List<Map<String, Object>> formattedMenus = formatMenus(allMenus);
		model.addAttribute("menus", formattedMenus);
		return "foodMenu/AllMenus";
	}

	// 找會員飲食紀錄 - 後台
	@GetMapping("/admin/foodMenu/getMenuByMember")
	public String getMenuByMember(@RequestParam("memNo") String memNoStr, Model model) {
		Integer memNo = Integer.parseInt(memNoStr);
		List<FoodMenu> menus = menuService.findMenusByMemberID(memNo);
		List<Map<String, Object>> formattedMenus = formatMenus(menus);
		model.addAttribute("menus", formattedMenus);
		return "foodMenu/AllMenus";
	}

	private List<Map<String, Object>> formatMenus(List<FoodMenu> menus) {
		Map<Integer, Map<String, Object>> menuMap = new HashMap<>();

		for (FoodMenu menu : menus) {
			Integer menuId = menu.getFoodMenuID();
			Map<String, Object> menuData = menuMap.computeIfAbsent(menuId, id -> {
				Map<String, Object> map = new HashMap<>();
				map.put("menuID", menuId);
				map.put("createdTime", menu.getCreateTime());
				map.put("memberName", menu.getMember().getMemName());
				map.put("details", new ArrayList<Map<String, Object>>());
				return map;
			});

			List<Map<String, Object>> details = (List<Map<String, Object>>) menuData.get("details");

			for (FoodMenuDetail detail : menu.getFoodMenuDetails()) {
				Map<String, Object> detailData = new HashMap<>();
				detailData.put("foodName", detail.getFood().getFoodName());
				detailData.put("foodWeight", detail.getFoodWeight());
				detailData.put("currentCalories", detail.getCalories());
				details.add(detailData);
			}

			menuData.put("totalCalories", menu.getTotalCal());
		}

		return new ArrayList<>(menuMap.values());
	}
	
	// 進 使用者飲食管理(個人) - 前台
	@GetMapping("/foodMenu/memberMenu")
	public String getMemberMenu(Model model,HttpSession httpSession,HttpServletResponse response) {
		MemberBean user = (MemberBean) httpSession.getAttribute("user");
		
		if (user == null) {
	        // 用戶未登錄，重定向到登錄頁面
	        return "redirect:/admin/index"; 
	    }
		
		List<FoodMenu> menus = menuService.findMenusByMemberID(user.getMemNo());
		List<Map<String, Object>> formattedMenus = formatMenu(menus);
		model.addAttribute("member", user);
		model.addAttribute("menus", formattedMenus);
		return "foodMenu/MemberMenu";
	}
	
	// 找會員飲食紀錄 - 前台
	@GetMapping("/foodMenu/getMemberMenu")
	public String getMenuByID(@RequestParam("memNo") String memNoStr, Model model) {
		Integer memNo = Integer.parseInt(memNoStr);
		List<FoodMenu> menus = menuService.findMenusByMemberID(memNo);
		List<Map<String, Object>> formattedMenus = formatMenu(menus);
		model.addAttribute("menus", formattedMenus);
		return "foodMenu/MemberMenu";
	}

	private List<Map<String, Object>> formatMenu(List<FoodMenu> menus) {
		Map<Integer, Map<String, Object>> menuMap = new HashMap<>();

		for (FoodMenu menu : menus) {
			Integer menuId = menu.getFoodMenuID();
			Map<String, Object> menuData = menuMap.computeIfAbsent(menuId, id -> {
				Map<String, Object> map = new HashMap<>();
				map.put("menuID", menuId);
				map.put("createdTime", menu.getCreateTime());
				map.put("memberName", menu.getMember().getMemName());
				map.put("details", new ArrayList<Map<String, Object>>());
				return map;
			});

			List<Map<String, Object>> details = (List<Map<String, Object>>) menuData.get("details");

			for (FoodMenuDetail detail : menu.getFoodMenuDetails()) {
				Map<String, Object> detailData = new HashMap<>();
				detailData.put("foodName", detail.getFood().getFoodName());
				detailData.put("foodWeight", detail.getFoodWeight());
				detailData.put("currentCalories", detail.getCalories());
				details.add(detailData);
			}

			menuData.put("totalCalories", menu.getTotalCal());
		}

		return new ArrayList<>(menuMap.values());
	}
	
//	// 查詢ByTime
//	@ResponseBody
//	@GetMapping("/api/getMenuCre")
//	public List<FoodMenu> getMenuCre(
//			@RequestParam("createTime") 
//			@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date createTime) {
//		List<FoodMenu> menus = menuService.findMenusByCreateTime(createTime);
//	    return menuService.findMenusByCreateTime(createTime);
//	}
}
