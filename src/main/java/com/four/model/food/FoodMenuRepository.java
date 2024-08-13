package com.four.model.food;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodMenuRepository extends JpaRepository<FoodMenu, Integer> {
	
	
    @Query("SELECT fm FROM FoodMenu fm JOIN FETCH fm.foodMenuDetails WHERE fm.member.memNo = :memNo")
	List<FoodMenu> findByMemberMemNo(Integer memNo);
	
	// 查詢ByTime
	@Query("SELECT f FROM FoodMenu f WHERE f.createTime = :createTime")
    List<FoodMenu> findByCreateTime(@Param("createTime") Date createTime);

}
