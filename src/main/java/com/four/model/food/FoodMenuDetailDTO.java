package com.four.model.food;
import com.four.model.memberAdm.MemberBean;

import lombok.AllArgsConstructor;
import lombok.Data;
//每筆 菜單明細的資料
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor //無參數建構子
@AllArgsConstructor//全參數建構子
public class FoodMenuDetailDTO {
	
	public FoodMenuDetailDTO(MemberBean member, Integer foodID, float foodWeight, float calories) {
		this.memberNo = member != null ? member.getMemNo() : null; // 根据你的 DTO 字段名称调整
	    this.foodID = foodID;
	    this.foodWeight = foodWeight;
	    this.calories = calories;
	}
	private Integer memberNo;
	private Integer foodID;
    private float foodWeight; //食品重量
    private float calories; //單筆食品熱量

}
