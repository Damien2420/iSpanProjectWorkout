package com.four.controller.course.course;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.course.course.Course;
import com.four.model.course.schedule.Schedule;
import com.four.service.course.course.CourseService;
import com.four.service.course.schedule.ScheduleService;

@Controller
@RequestMapping("/admin")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ScheduleService scheduleService;
	
//	Case : Return Page	
	@GetMapping("/course/CourseGetAll")
	public String findAll() {
		return "course/course/back/CourseGetAll";
	}
	@GetMapping("/course/CourseGetById")
	public String findById(@RequestParam Integer courseId,Model model) {
		Course course = courseService.findByCourseId(courseId);
		model.addAttribute("course", course);	
		return "course/course/back/CourseGetById";
	}
// 	Case : ResponseBody
	@ResponseBody
	@GetMapping("/course/api/CourseFindAll")
	public List<Course> CourseFindAll(){
		return courseService.findAll();
	}
	@ResponseBody
	@GetMapping("/course/api/CourseFindByCurrentDate")
	public List<Course> findByCurrentDate(@RequestParam LocalDate currentDate){
		return courseService.findByCurrentDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/course/api/CourseFindByPastDate")
	public List<Course> findByPastDate(@RequestParam LocalDate currentDate){
		return courseService.findByPastDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/course/api/CourseFindByFutureDate")
	public List<Course> findByFutureDate(@RequestParam LocalDate currentDate){
		return courseService.findByFutureDate(currentDate);
	}
	@ResponseBody
	@GetMapping("/course/api/CourseFindByLike")
	public List<Course> CourseFindByLike(@RequestParam String likeword){
		return courseService.findByLikeWord(likeword);
	}
	@ResponseBody
	@GetMapping("/course/api/CourseFindById")
	public Course CourseFindById(@RequestParam Integer courseId) {
		return courseService.findByCourseId(courseId);
	}
	@ResponseBody
	@GetMapping("/course/api/CourseFindByName")
	public Course CourseFindByName(@RequestParam String courseName) {
		return courseService.findByCourseName(courseName);
	}
	@ResponseBody
	@GetMapping("/course/api/CourseDelete")
	public String CourseDelete(@RequestParam Integer courseId) {
		List<Schedule> Schedules = scheduleService.findByCourseId(courseId);
		if(Schedules.isEmpty()) {
			courseService.Delete(courseId);
			return "1";
		}else {
			return "0";

		}
	}
	@ResponseBody
	@PostMapping("/course/api/CourseInsertmain")
	public void CourseInsertMain(@RequestParam("courseName") String courseName,@RequestParam("courseType") String courseType,@RequestParam("coursePrice") Integer coursePrice,@RequestParam("courseStartDate") String courseStartDateStr,@RequestParam("courseProfile") String courseProfile,@RequestParam("coachId") Integer coachId) {		
        Date courseStartDate = setStartDate(courseStartDateStr);
        Date courseEndDate = setEndDate(courseStartDate);
        String courseStatus = setStatus(courseStartDate,courseEndDate);
        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseType(courseType);
        course.setCoursePrice(coursePrice);
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);
        course.setCourseStatus(courseStatus);
        course.setCourseProfile(courseProfile);
        course.setCoachId(coachId);
        courseService.Save(course);
	}
	@ResponseBody
	@GetMapping("/course/api/CourseUpdate")
	public Course CourseUpdate(@RequestParam Integer courseId) {
		return courseService.findByCourseId(courseId);
	}
	@ResponseBody
	@PostMapping("/course/api/CourseUpdateMain")
	public void CourseUpdateMain(@RequestParam("courseId") Integer courseId,@RequestParam("courseName") String courseName,@RequestParam("courseType") String courseType,@RequestParam("coursePrice") Integer coursePrice,@RequestParam("courseStartDate") String courseStartDateStr,@RequestParam("courseProfile") String courseProfile,@RequestParam("coachId") Integer coachId){		
		Course course = courseService.findByCourseId(courseId);
        Date courseStartDate = setStartDate(courseStartDateStr);
        Date courseEndDate = setEndDate(courseStartDate);
        String courseStatus = setStatus(courseStartDate,courseEndDate);
        course.setCoachId(coachId);
        course.setCourseName(courseName);
        course.setCourseType(courseType);
        course.setCoursePrice(coursePrice);
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);
        course.setCourseStatus(courseStatus);
        course.setCourseProfile(courseProfile);
        course.setCoachId(coachId);
		courseService.Save(course);
	}
// Case : Function
    private Date setStartDate(String courseStartDateStr) {
        YearMonth yearMonth = YearMonth.parse(courseStartDateStr, DateTimeFormatter.ofPattern("yyyy-MM"));
        return Date.valueOf(yearMonth.atDay(1));
    }
    private Date setEndDate(Date courseStartDate) {
        LocalDate startDate = courseStartDate.toLocalDate();
        YearMonth yearMonth = YearMonth.from(startDate);
        YearMonth nextMonth = yearMonth.plusMonths(1);
        LocalDate endDate = nextMonth.atEndOfMonth();
        return Date.valueOf(endDate);
    }
    private String setStatus(Date courseStartDate, Date courseEndDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = courseStartDate.toLocalDate();
        LocalDate endDate = courseEndDate.toLocalDate();
        if (currentDate.isBefore(startDate)) {return "unactive";} 
        else if (currentDate.isAfter(endDate)) {return "unactive";} 
        else {return "active";}
    }
}
