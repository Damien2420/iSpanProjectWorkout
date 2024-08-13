package com.four.model.food;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor //無參數建構子
@AllArgsConstructor//全參數建構子
public class FoodMenuDTO {
	
	private Integer foodMenuID;
    private String memberName;
    private float totalCal;
    private Date createTime;
    private List<FoodMenuDetailDTO> foodMenuDetails;
}
