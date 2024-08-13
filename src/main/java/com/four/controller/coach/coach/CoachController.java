package com.four.controller.coach.coach;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.four.model.coach.coach.Coach;
import com.four.model.course.course.Course;
import com.four.service.coach.coach.CoachService;
import com.four.service.course.course.CourseService;

@Controller
@RequestMapping("/admin")
public class CoachController {

	@Autowired
	private CoachService coachService;
	
	@Autowired
	private CourseService courseService;
//	Case : Return Page
	@GetMapping("/coach/CoachGetAll")
	public String findAll() {
		return "coach/back/CoachGetAll";
	}
	@GetMapping("/coach/CoachGetById")
	public String findById(@RequestParam Integer coachId,Model model) {
		Coach coach = coachService.findById(coachId);
		model.addAttribute("coach", coach);	
		return "coach/back/CoachGetById";
	}
// 	Case : ResponseBody
	@ResponseBody
	@GetMapping("/coach/api/CoachFindAll")
	public List<Coach> CoachFindAll(){
		return coachService.findAll();
	}
	@ResponseBody
	@GetMapping("/coach/api/CoachFindByLike")
	public List<Coach> CoachFindByLike(@RequestParam String likeword){
		return coachService.findByLikeWord(likeword);
	}
	@ResponseBody
	@GetMapping("/coach/api/CoachDelete")
	public String Delete(@RequestParam Integer coachId) {
		List<Course> courses = courseService.findByCoachId(coachId);
		if(courses.isEmpty()) {
			coachService.Delete(coachId);
			return "1";
		}else {
			return "0";
		}
	}
	@ResponseBody
	@PostMapping("/coach/api/CoachInsertMain")
	public String CoachInsert(@RequestParam("coachName") String coachName,@RequestParam("coachEmail") String coachEmail,@RequestParam("coachPhone") String coachPhone,@RequestParam("coachPic") MultipartFile coachPic,RedirectAttributes redirectAttributes) throws IOException {		
		Coach coach = new Coach();
		coach.setCoachName(coachName);
		coach.setCoachEmail(coachEmail);
		coach.setCoachPhone(coachPhone);
		coach.setCoachPic(coachPic.getBytes());
		coachService.Save(coach);
		return null;
	}
	@ResponseBody
	@GetMapping("/coach/api/CoachUpdate")
	public Coach CoachUpdate(@RequestParam Integer coachId) {
		return coachService.findById(coachId);
	}
	@ResponseBody
	@PostMapping("/coach/api/CoachUpdateMain")
	public String CoachUpdateMain(@RequestParam("coachId") Integer coachId,@RequestParam("coachName") String coachName,@RequestParam("coachEmail") String coachEmail,@RequestParam("coachPhone") String coachPhone,@RequestParam("coachPic") MultipartFile coachPic,RedirectAttributes redirectAttributes) throws IOException {		
		Coach coach = coachService.findById(coachId);
		coach.setCoachName(coachName);
		coach.setCoachEmail(coachEmail);
		coach.setCoachPhone(coachPhone);
		if(!coachPic.isEmpty()) {
			coach.setCoachPic(coachPic.getBytes());			
		}
		coachService.Save(coach);
		return null;
	}
// Case : ResponseEntity
	@GetMapping("/coach/coachPhotos")
	public ResponseEntity<byte[]> coachPhotos(@RequestParam Integer coachId) {
		Coach coach = coachService.findById(coachId);
		byte[] coachPic = coach.getCoachPic();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(coachPic, headers, HttpStatus.OK);	
	}
}
