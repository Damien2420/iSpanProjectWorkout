package com.four.controller.course.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.course.course.Course;
import com.four.model.course.course.CourseDTO;
import com.four.model.course.schedule.Schedule;
import com.four.service.course.course.CourseService;
import com.four.service.course.schedule.ScheduleService;

@Controller
@RequestMapping("/admin")
public class ScheduleController {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ScheduleService scheduleService;
	
//	Case : Return Page		
	@GetMapping("/schedule/ScheduleGetAll")
	public String GetAll() {
		return "course/schedule/back/ScheduleGetAll";
	}
	@GetMapping("/schedule/ScheduleList")
	public String findAllGroupByCourseId() {
		return "course/schedule/back/ScheduleList";
	}
	
// 	Case : ResponseBody
	@ResponseBody
	@GetMapping("/schedule/api/ScheduleFindAll")
	public List<Schedule> ScheduleFindAll(){
		return scheduleService.findAll();
	}
	@ResponseBody
	@GetMapping("/schedule/api/CourseFindByCurrentDate")
	public List<Schedule> findByCurrentDate(@RequestParam LocalDate currentDate){
		return scheduleService.findByCurrentDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/schedule/api/CourseFindByPastDate")
	public List<Schedule> findByPastDate(@RequestParam LocalDate currentDate){
		return scheduleService.findByPastDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/schedule/api/CourseFindByFutureDate")
	public List<Schedule> findByFutureDate(@RequestParam LocalDate currentDate){
		return scheduleService.findByFutureDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/schedule/api/ScheduleFindByCourseId")
	public List<Schedule> ScheduleFindByCourseId(@RequestParam("courseId") Integer courseId){
		return scheduleService.findByCourseId(courseId);
	}
	@ResponseBody	
	@GetMapping("/schedule/api/ScheduleDelete")
	public String delete(@RequestParam("scheduleId") Integer scheduleId) {
		scheduleService.Delete(scheduleId);
		return "0";
	}
	@ResponseBody
	@PostMapping("/schedule/api/ScheduleInsert")
	public String InsertMain(@RequestParam("weekday") String weekday, @RequestParam("daytime") String daytime, @RequestParam("courseId") Integer courseId){
	    Schedule schedule = new Schedule();
	    List<Schedule> schedules = scheduleService.findByCourseId(courseId);
	    for (Schedule schedule2 : schedules) {
			if(schedule2.getWeekday().equals(weekday) && schedule2.getDaytime().equals(daytime)) {
				return "0";
			}
		}
	    schedule.setWeekday(weekday);
	    schedule.setDaytime(daytime);
	    schedule.setCourseId(courseId);
	    scheduleService.Save(schedule);
	    return "1";
	}	
	
// Case : Test
	
	@ResponseBody
	@GetMapping("/schedule/api/DTO/CourseFindAllWithSchedule")
	public List<CourseDTO> CourseFindAllWithSchedule(){
		List<Course> courses = courseService.findAll();
		List<CourseDTO> courseDTOs = CourseDataTransferObjectList(courses);
		return courseDTOs;
	}
	@ResponseBody
	@GetMapping("/schedule/api/DTO/CourseFindByCurrentDatelWithSchedule")
	public List<CourseDTO> CourseFindByCurrentDatelWithSchedule(@RequestParam LocalDate currentDate){
		List<Course> courses = courseService.findByCurrentDate(currentDate);
		List<CourseDTO> courseDTOs = CourseDataTransferObjectList(courses);
		return courseDTOs;
	}
	@ResponseBody
	@GetMapping("/schedule/api/DTO/CourseFindByPastDatelWithSchedule")
	public List<CourseDTO> CourseFindByPastDatelWithSchedule(@RequestParam LocalDate currentDate){
		List<Course> courses = courseService.findByPastDate(currentDate);
		List<CourseDTO> courseDTOs = CourseDataTransferObjectList(courses);
		return courseDTOs;
	}
	@ResponseBody
	@GetMapping("/schedule/api/DTO/CourseFindByFutureDatelWithSchedule")
	public List<CourseDTO> CourseFindByFutureDatelWithSchedule(@RequestParam LocalDate currentDate){
		List<Course> courses = courseService.findByFutureDate(currentDate);
		List<CourseDTO> courseDTOs = CourseDataTransferObjectList(courses);
		return courseDTOs;
	}
	
// Case : Function	
	public CourseDTO CourseDataTransferObject(Course course) {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setCourseId(course.getCourseId());
		courseDTO.setCourseName(course.getCourseName());
		courseDTO.setCoursePrice(course.getCoursePrice());
		courseDTO.setCourseStartDate(course.getCourseStartDate());
		courseDTO.setCourseEndDate(course.getCourseEndDate());
		courseDTO.setCoach(course.getCoach());
		courseDTO.setSchedules(course.getSchedules());
		return courseDTO;
	}
	public List<CourseDTO> CourseDataTransferObjectList(List<Course> courses) {
		List<CourseDTO> courseDTOs = new ArrayList<>();
		for (Course course : courses) {
			CourseDTO courseDto =CourseDataTransferObject(course);
			courseDTOs.add(courseDto);
		}
		return courseDTOs;
	}
	
}
