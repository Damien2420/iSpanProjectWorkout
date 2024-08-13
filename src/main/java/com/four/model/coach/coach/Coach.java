package com.four.model.coach.coach;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.four.model.coach.coachSpecialties.CoachSpecialties;
import com.four.model.course.course.Course;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter @Entity @Table(name = "coach")
public class Coach implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer coachId;
	
	private String coachName;
	
	private String coachEmail;
	
	private String coachPhone;
	
	@JsonIgnore @Lob
	private byte[] coachPic;
	
	@JsonIgnore @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
	private List<Course> courses = new ArrayList<>();
	
	@JsonIgnore @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
	private List<CoachSpecialties> coachSpecialties = new ArrayList<>();
	
}