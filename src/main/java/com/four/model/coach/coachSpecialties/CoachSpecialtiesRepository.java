package com.four.model.coach.coachSpecialties;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoachSpecialtiesRepository extends JpaRepository<CoachSpecialties, Integer>  {

    @Override
    @Query("SELECT DISTINCT cs FROM CoachSpecialties cs LEFT JOIN FETCH cs.coach c")
    List<CoachSpecialties> findAll();
	
	@Query("FROM CoachSpecialties WHERE coachId=:coachId")
	List<CoachSpecialties> findByCoachId(@Param("coachId") Integer coachId);
	
}
