package com.four.controller.coach.coach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.coach.coach.Coach;
import com.four.model.coach.coach.CoachDTO;
import com.four.service.coach.coach.CoachService;

@Controller
public class CoachControllerF {
	
	@Autowired
	private CoachService coachService;
	
//	Case : Return Page
	@GetMapping("/coach/front/CoachFront")
	public String CoachFront() {
		return "coach/front/CoachFront";
	}
// 	Case : ResponseBody
	@ResponseBody
	@GetMapping("/coach/front/api/DTO/CoachFindAllDTOByCurrentDate")
	public List<CoachDTO> findAllDTOByCurrentDate() {
		List<Coach> coachs = coachService.findByCurrentDate(LocalDate.now());
		List<CoachDTO> coachDTOs = CoachDataTransferObjectList(coachs);
		return coachDTOs;
	}
	
// 	Case : Function	
	public CoachDTO CoachDataTransferObject(Coach coach) {
		CoachDTO coachDTO = new CoachDTO();
		coachDTO.setCoachId(coach.getCoachId());
		coachDTO.setCoachName(coach.getCoachName());
		coachDTO.setCoachEmail(coach.getCoachEmail());
		coachDTO.setCoachPhone(coach.getCoachPhone());
		coachDTO.setCourses(coach.getCourses());
		coachDTO.setCoachSpecialties(coach.getCoachSpecialties());
		return coachDTO;
	}
	public List<CoachDTO> CoachDataTransferObjectList(List<Coach> coachs) {
		List<CoachDTO> coachDTOs = new ArrayList<>();
		for (Coach coach : coachs) {
			CoachDTO coachDTO =CoachDataTransferObject(coach);
			coachDTOs.add(coachDTO);
		}
		return coachDTOs;
	}
	// Case : ResponseEntity
	@GetMapping("/coach/front/api/coachPhotos")
	public ResponseEntity<byte[]> findCoachPhotos(@RequestParam Integer coachId) {
		Coach coach = coachService.findById(coachId);
		byte[] coachPic = coach.getCoachPic();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(coachPic, headers, HttpStatus.OK);	
	}
}
