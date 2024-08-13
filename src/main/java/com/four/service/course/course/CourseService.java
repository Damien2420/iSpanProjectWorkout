package com.four.service.course.course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.four.model.course.course.Course;
import com.four.model.course.course.CourseRepository;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;
	
	public List<Course> findAll() {
		return courseRepository.findAll();
	}
	
	public Course findByCourseId(Integer id) {
		Optional<Course> optional = courseRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public Course findByCourseName(String name) {
	    List<Course> courses = courseRepository.findByCourseName(name);
	    return courses.isEmpty() ? null : courses.get(0);
	}
	
	public List<Course> findByLikeWord(String likeword) {
		return courseRepository.findByLikeWord(likeword);	
	}
	
	public List<Course> findByCoachId(Integer id) {
		return courseRepository.findByCoachId(id);
	}
	
	public void Save(Course course) {
		courseRepository.save(course);
	}
	
	public void Delete(Integer courseId) {
		courseRepository.deleteById(courseId);
	}
	
	public List<Course> findByCurrentDate(LocalDate currentDate) {
		return courseRepository.findByCurrentDate(currentDate);
	}
	public List<Course> findByPastDate(LocalDate currentDate) {
		return courseRepository.findByPastDate(currentDate);
	}
	public List<Course> findByFutureDate(LocalDate currentDate) {
		return courseRepository.findByFutureDate(currentDate);
	}
}
