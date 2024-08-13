package com.four.service.coach.coachSpecialties;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.four.model.coach.coachSpecialties.CoachSpecialties;
import com.four.model.coach.coachSpecialties.CoachSpecialtiesRepository;

@Service
public class CoachSpecialtiesService {

	@Autowired
	private CoachSpecialtiesRepository coachSpecialtiesRepository;
	
	public List<CoachSpecialties> findByCoachId(Integer coachId) {
		return coachSpecialtiesRepository.findByCoachId(coachId);
	}
	
	public void Save(CoachSpecialties coachSpecialties) {
		coachSpecialtiesRepository.save(coachSpecialties);
	}
	
	public void Delete(Integer specialtyId) {
		coachSpecialtiesRepository.deleteById(specialtyId);;
	}
	
}
