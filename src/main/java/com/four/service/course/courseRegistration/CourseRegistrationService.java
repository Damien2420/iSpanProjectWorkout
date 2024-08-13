package com.four.service.course.courseRegistration;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.four.model.course.courseRegistration.CourseRegistration;
import com.four.model.course.courseRegistration.CourseRegistrationRepository;

@Service
public class CourseRegistrationService {

	@Autowired
	private CourseRegistrationRepository courseRegistrationRepository;

	public List<CourseRegistration> findAll() {
		return courseRegistrationRepository.findAll();
	}
	public List<CourseRegistration> findByCourseId(Integer courseId) {
		return courseRegistrationRepository.findByCourseId(courseId);
	}
	public List<CourseRegistration> findByMemberId(Integer memberId) {
		return courseRegistrationRepository.findByMemberId(memberId);
	}
	public CourseRegistration findByRegisterID(Integer registerId) {
		return courseRegistrationRepository.findByRegisterID(registerId);
	}
	public void Save(CourseRegistration courseRegistration) {
		courseRegistrationRepository.save(courseRegistration);
	}
	public void deleteById(Integer registerID) {
		courseRegistrationRepository.deleteById(registerID);
	}
	public List<CourseRegistration> findByCurrentDate(LocalDate currentDate) {
		return courseRegistrationRepository.findByCurrentDate(currentDate);
	}
	public List<CourseRegistration> findByPastDate(LocalDate currentDate) {
		return courseRegistrationRepository.findByPastDate(currentDate);
	}
	public List<CourseRegistration> findByFutureDate(LocalDate currentDate) {
		return courseRegistrationRepository.findByFutureDate(currentDate);
	}
	
}
