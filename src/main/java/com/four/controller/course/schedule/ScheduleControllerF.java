package com.four.controller.course.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.course.course.Course;
import com.four.model.course.course.CourseDTO;
import com.four.model.course.schedule.Schedule;
import com.four.model.memberAdm.MemberBean;
import com.four.service.course.course.CourseService;
import com.four.service.course.schedule.ScheduleService;

import jakarta.servlet.http.HttpSession;



@Controller
public class ScheduleControllerF {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ScheduleController scheduleController;
	
// Member
	@ResponseBody
	@GetMapping("/login")
	public void loginPrw(HttpSession httpSession) {
		MemberBean member = scheduleService.checkLogin();
        httpSession.setAttribute("user", member);
        System.out.println(member);
	}
	
//	Case : Return Page
	
	@GetMapping("/course/front/CourseFront")
	public String CourseFront() {
		return "course/course/front/CourseFront";
	}
	
	@GetMapping("/schedule/front/SchedulePreSelection")
	public String SchedulePreSelection(Model m,HttpSession session) {
		MemberBean user = (MemberBean) session.getAttribute("user");
		m.addAttribute("member",user);
		return "course/schedule/front/SchedulePreSelection";
	}
	
	@GetMapping("/schedule/front/ScheduleShowByMemberId")
	public String ScheduleShowByMemberId(Model m,HttpSession session) {
		MemberBean user = (MemberBean) session.getAttribute("user");
		m.addAttribute("member",user);
		return "course/schedule/front/ScheduleShowByMemberId";
	}
	
// 	Case : ResponseBody	
	@ResponseBody
	@GetMapping("/schedule/front/api/ScheduleFindByCourseIds")
	public List<Schedule> ScheduleFindByCourseIds(@RequestParam("courseIds") Integer[] courseIds){
		return scheduleService.findByCourseIds(courseIds);
	}
	
	@ResponseBody
	@GetMapping("/schedule/front/api/ScheduleFindByCourseId")
	public List<Schedule> ScheduleFindByCourseId(@RequestParam("courseId") Integer courseId){
		return scheduleService.findByCourseId(courseId);
	}
	
// 	Case : ResponseBody
	@ResponseBody
	@GetMapping("/course/front/api/CourseFindAll")
	public List<Course> CourseFindAll(){
		return courseService.findAll();
	}
	@ResponseBody
	@GetMapping("/course/front/api/CourseFindByCurrentDate")
	public List<Course> findByCurrentDate(@RequestParam LocalDate currentDate){
		return courseService.findByCurrentDate(currentDate);
	}
	
	@ResponseBody
	@GetMapping("/schedule/front/api/DTO/CourseFindByFutureDatelWithSchedule")
	public List<CourseDTO> CourseFindByFutureDatelWithSchedule(@RequestParam LocalDate currentDate){
		List<Course> courses = courseService.findByFutureDate(currentDate);
		List<CourseDTO> courseDTOs = scheduleController.CourseDataTransferObjectList(courses);
		return courseDTOs;
	}
	
	
}
