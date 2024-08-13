package com.four.model.food;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

//食物總表
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor //無參數建構子
@AllArgsConstructor//全參數建構子
@Setter
@Getter
@Entity
@Table(name = "food")
public class Food {
	
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer foodID;
	
	private String foodName;
	private Integer category; //分類
	private float protein;
	private float carbohydrates;
	private float fat;
	private float totalCaloriesPer100g;
	
	@JsonIgnore @OneToMany(mappedBy = "food")
    private List<FoodMenuDetail> foodMenuDetails = new ArrayList<>();
	
	//計算100g總熱量
	public float calculateTotalCaloriesPer100g() {
		float totalCaloriesPer100g = protein * 4 + carbohydrates * 4 + fat * 9;
        return Float.parseFloat(String.format("%.2f", totalCaloriesPer100g));
    }

}
