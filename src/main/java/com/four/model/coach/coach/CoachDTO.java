package com.four.model.coach.coach;

import java.util.ArrayList;
import java.util.List;

import com.four.model.coach.coachSpecialties.CoachSpecialties;
import com.four.model.course.course.Course;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class CoachDTO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer coachId;
	
	private String coachName;
	
	private String coachEmail;
	
	private String coachPhone;
	
	@OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
	private List<Course> courses = new ArrayList<>();
	
	@OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
	private List<CoachSpecialties> coachSpecialties = new ArrayList<>();
	
}