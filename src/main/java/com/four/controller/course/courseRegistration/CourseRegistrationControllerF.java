package com.four.controller.course.courseRegistration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.course.course.Course;
import com.four.model.course.courseRegistration.CourseRegistration;
import com.four.model.memberAdm.MemberBean;
import com.four.service.course.course.CourseService;
import com.four.service.course.courseRegistration.CourseRegistrationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CourseRegistrationControllerF {

	@Autowired
	private CourseRegistrationService courseRegistrationService;
	
	@Autowired
	private CourseService courseService;

	@GetMapping("/courseRegistration/front/CourseRegistrationGetByMemberId")
	public String findAllGroupByCourseId(Model m,HttpSession session) {
		MemberBean user = (MemberBean) session.getAttribute("user");
		m.addAttribute("member",user);
		return "course/courseRegistration/front/CourseRegistrationGetByMemberId";
	}
	@ResponseBody
	@GetMapping("/courseRegistration/front/api/CourseRegistrationGetByMemberId")
	public List<CourseRegistration> findByMemberId(@RequestParam("memberId") Integer memberId) {
		return courseRegistrationService.findByMemberId(memberId);
	}
	@ResponseBody
	@PostMapping("/courseRegistration/front/api/CourseRegistrationInsert")
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
	@GetMapping("/courseRegistration/front/api/CourseRegistrationCheck")
	public void Check(@RequestParam("registerID") Integer registerID,@RequestParam("registerStatus") String registerStatus){
		CourseRegistration courseRegistration = courseRegistrationService.findByRegisterID(registerID);
		courseRegistration.setRegisterStatus(registerStatus);
		courseRegistrationService.Save(courseRegistration);
	}
	
}
