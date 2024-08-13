package com.four.model.coach.coach;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoachRepository extends JpaRepository<Coach, Integer> {
	
	@Query("FROM Coach WHERE coachName LIKE %:likeword%")
	List<Coach> findByLikeWord(@Param(value = "likeword") String likeword);
	
	@Query("SELECT DISTINCT c FROM Coach c JOIN FETCH c.courses cr WHERE cr.courseStartDate <= :currentDate AND cr.courseEndDate >= :currentDate")
	List<Coach> findByCurrentDate(@Param("currentDate") LocalDate currentDate);
}
