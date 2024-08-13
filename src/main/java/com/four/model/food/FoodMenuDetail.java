package com.four.model.food;
import com.fasterxml.jackson.annotation.JsonBackReference;

//中介表格 - 菜單明細
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "foodMenuDetail")
public class FoodMenuDetail {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer foodMenuDetailID;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodMenuID", nullable = false)//不允許空值
    @JsonBackReference
    private FoodMenu foodMenu;//菜單編號

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodID", nullable = false)//不允許空值
    private Food food;//食物編號
    
    private float calories; //單筆食品熱量
    
    private float foodWeight; // 食品重量

	public FoodMenuDetail(FoodMenu foodMenu, Food food, float calories, float foodWeight) {
		this.foodMenu = foodMenu;
		this.food = food;
		this.calories = calories;
		this.foodWeight = foodWeight;
	}


    
    
}
