package com.four.model.course.course;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Integer> {
	
	@Query("SELECT c FROM Course c LEFT JOIN FETCH c.coach WHERE c.courseName LIKE %:likeword% OR c.courseType LIKE %:likeword%")
	List<Course> findByLikeWord(@Param(value = "likeword") String likeword);

	@Query("SELECT c FROM Course c LEFT JOIN FETCH c.coach WHERE c.coachId = :id")
	List<Course> findByCoachId(@Param(value = "id") Integer id);
	
	List<Course> findByCourseName(String courseName);

    @Query("SELECT c FROM Course c WHERE c.courseStartDate <= :currentDate AND c.courseEndDate >= :currentDate")
    List<Course> findByCurrentDate(@Param("currentDate") LocalDate currentDate);
    @Query("SELECT c FROM Course c WHERE c.courseEndDate < :currentDate")
    List<Course> findByPastDate(@Param("currentDate") LocalDate currentDate);
    @Query("SELECT c FROM Course c WHERE c.courseStartDate > :currentDate")
    List<Course> findByFutureDate(@Param("currentDate") LocalDate currentDate);

}
