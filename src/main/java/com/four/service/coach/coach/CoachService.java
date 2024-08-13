package com.four.service.coach.coach;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.four.model.coach.coach.Coach;
import com.four.model.coach.coach.CoachRepository;

@Service
public class CoachService {
	
	@Autowired
	CoachRepository coachRepository;
	
	public List<Coach> findAll() {
		return coachRepository.findAll();
	}
	
	public Coach findById(Integer id) {
		Optional<Coach> optional = coachRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<Coach> findByLikeWord(String likeword) {
		return coachRepository.findByLikeWord(likeword);
	}
	
	public void Save(Coach coach) {
		coachRepository.save(coach);
	}
	
	public void Delete(Integer coachId) {
		coachRepository.deleteById(coachId);
	}
	
	//front
	public List<Coach> findByCurrentDate(LocalDate currentDate) {
		return coachRepository.findByCurrentDate(currentDate);
	}
}
