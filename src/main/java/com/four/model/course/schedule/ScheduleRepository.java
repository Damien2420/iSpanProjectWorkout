package com.four.model.course.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
	
    @Override
    @Query("SELECT DISTINCT s FROM Schedule s LEFT JOIN FETCH s.course c")
    List<Schedule> findAll();
	
	@Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.course c WHERE c.courseId = :id")
	List<Schedule> findByCourseId(@Param(value = "id") Integer id);

//	@Query("SELECT c.courseId, c.courseName, STRING_AGG(s.scheduleId, ', ') AS scheduleIds, STRING_AGG(s.weekday, ', ') AS weekdays, STRING_AGG(s.daytime, ', ') AS daytimes, ch.coachId, ch.coachName FROM course c JOIN schedule s ON c.courseId = s.courseId JOIN coach ch ON c.coachId = ch.coachId GROUP BY c.courseId, c.courseName, ch.coachId, ch.coachName")
//	List<Object[]> findAllGroupByCourseId();
	
	@Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.course c WHERE c.courseId IN (:ids)")
	List<Schedule> findByCourseIds(@Param("ids") Integer[] ids);
	
    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.course c WHERE c.courseStartDate <= :currentDate AND c.courseEndDate >= :currentDate")
    List<Schedule> findByCurrentDate(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.course c WHERE c.courseEndDate < :currentDate")
    List<Schedule> findByPastDate(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.course c WHERE c.courseStartDate > :currentDate")
    List<Schedule> findByFutureDate(@Param("currentDate") LocalDate currentDate);
    
}
