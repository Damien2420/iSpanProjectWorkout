package com.four.controller.course.courseRegistration;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.course.course.Course;
import com.four.model.course.courseRegistration.CourseRegistration;
import com.four.service.course.course.CourseService;
import com.four.service.course.courseRegistration.CourseRegistrationService;

@Controller
@RequestMapping("/admin")
public class CourseRegistrationController {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseRegistrationService courseRegistrationService;
	
	@GetMapping("/courseRegistration/CourseRegistrationGetAll")
	public String courseRegistrationGetAll() {
		return "course/courseRegistration/back/CourseRegistrationGetAll";
	}
	@ResponseBody
	@GetMapping("/courseRegistration/api/CourseRegistrationGetAll")
	public List<CourseRegistration> findAll() {
		return courseRegistrationService.findAll();
	}
	@ResponseBody
	@GetMapping("/courseRegistration/api/CourseFindByCurrentDate")
	public List<CourseRegistration> findByCurrentDate(@RequestParam LocalDate currentDate){
		return courseRegistrationService.findByCurrentDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/courseRegistration/api/CourseFindByPastDate")
	public List<CourseRegistration> findByPastDate(@RequestParam LocalDate currentDate){
		return courseRegistrationService.findByPastDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/courseRegistration/api/CourseFindByFutureDate")
	public List<CourseRegistration> findByFutureDate(@RequestParam LocalDate currentDate){
		return courseRegistrationService.findByFutureDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/courseRegistration/api/CourseRegistrationGetByCourseId")
	public List<CourseRegistration> findByCourseId(@RequestParam("courseId") Integer courseId) {
		return courseRegistrationService.findByCourseId(courseId);
	}
	@ResponseBody
	@GetMapping("/courseRegistration/api/CourseRegistrationGetByMemberId")
	public List<CourseRegistration> findByMemberId(@RequestParam("memberId") Integer memberId) {
		return courseRegistrationService.findByMemberId(memberId);
	}
	
	@ResponseBody
	@GetMapping("/courseRegistration/api/CourseRegistrationDelete")
	public String Delete(@RequestParam Integer registerID) {
		courseRegistrationService.deleteById(registerID);
		return "1" ;
	}
	
	@ResponseBody
	@PostMapping("/courseRegistration/api/CourseRegistrationInsert")
	public String InsertMain(@RequestParam("courseId") Integer courseId,@RequestParam("memberId") Integer memberId) {
	    List<CourseRegistration> courseRegistrations = courseRegistrationService.findByMemberId(memberId);
	    for (CourseRegistration registration : courseRegistrations) {
	        if (registration.getCourseId().equals(courseId)) {
	            return "0";
	        }
	    }
	    CourseRegistration newRegistration = new CourseRegistration();
	    newRegistration.setCourseId(courseId);
	    newRegistration.setMemberId(memberId);
	    Course course = courseService.findByCourseId(courseId);	    
	    newRegistration.setCoursePrice(course.getCoursePrice());
	    newRegistration.setRegisterStatus("待審核");
        courseRegistrationService.Save(newRegistration);
        return "1";
	}
	@ResponseBody
	@GetMapping("/courseRegistration/api/CourseRegistrationCheck")
	public void Check(@RequestParam("registerID") Integer registerID,@RequestParam("registerStatus") String registerStatus){
		CourseRegistration courseRegistration = courseRegistrationService.findByRegisterID(registerID);
		courseRegistration.setRegisterStatus(registerStatus);
		courseRegistrationService.Save(courseRegistration);
	}
}
