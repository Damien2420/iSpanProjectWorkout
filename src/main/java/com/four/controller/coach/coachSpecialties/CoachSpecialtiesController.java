package com.four.controller.coach.coachSpecialties;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.coach.coachSpecialties.CoachSpecialties;
import com.four.service.coach.coachSpecialties.CoachSpecialtiesService;

@Controller
@RequestMapping("/admin")
public class CoachSpecialtiesController {

	@Autowired
	private CoachSpecialtiesService coachSpecialtiesService;
	
	
// 	Case : ResponseBody
	@ResponseBody
	@GetMapping("/coachSpecialties/api/CoachSpecialtiesFindByCoachId")
	public List<CoachSpecialties> CoachSpecialtiesFindByCoachId(Integer coachId){
		return coachSpecialtiesService.findByCoachId(coachId);
	}
	@ResponseBody
	@DeleteMapping("/coachSpecialties/api/CoachSpecialtiesDeleteBySpecialtyId/{specialtyId}")
	public void CoachSpecialtiesDeleteBySpecialtyId(@PathVariable Integer specialtyId) {
	    coachSpecialtiesService.Delete(specialtyId);
	}
	@ResponseBody
	@GetMapping("/coachSpecialties/api/CoachSpecialtiesInsert")
	public void CoachSpecialtiesInsert(@RequestParam("coachId") Integer coachId,@RequestParam("specialty") String specialty){		
		CoachSpecialties coachSpecialties = new CoachSpecialties();
		coachSpecialties.setCoachId(coachId);
		coachSpecialties.setSpecialty(specialty);
		coachSpecialtiesService.Save(coachSpecialties);
	}
}
