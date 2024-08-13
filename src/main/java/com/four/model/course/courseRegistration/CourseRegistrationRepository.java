package com.four.model.course.courseRegistration;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Integer> {
	
//    @Override
//    @Query("SELECT DISTINCT cr FROM CourseRegistration cr LEFT JOIN FETCH cr.course c LEFT JOIN FETCH cr.member m")
//    List<CourseRegistration> findAll();

	CourseRegistration findByRegisterID(Integer registerID);
	
	List<CourseRegistration> findByCourseId(Integer courseId);
	
	List<CourseRegistration> findByMemberId(Integer memberId);
	
    @Query("SELECT cr FROM CourseRegistration cr LEFT JOIN FETCH cr.course c WHERE c.courseStartDate <= :currentDate AND c.courseEndDate >= :currentDate")
    List<CourseRegistration> findByCurrentDate(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT cr FROM CourseRegistration cr LEFT JOIN FETCH cr.course c WHERE c.courseEndDate < :currentDate")
    List<CourseRegistration> findByPastDate(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT cr FROM CourseRegistration cr LEFT JOIN FETCH cr.course c WHERE c.courseStartDate > :currentDate")
    List<CourseRegistration> findByFutureDate(@Param("currentDate") LocalDate currentDate);
	
}
