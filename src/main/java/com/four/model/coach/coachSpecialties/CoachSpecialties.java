package com.four.model.coach.coachSpecialties;

import com.four.model.coach.coach.Coach;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter @Entity @Table(name = "coachSpecialties")
public class CoachSpecialties implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer specialtyId;
	
	private Integer coachId;
	
	private String specialty;
	
    @ManyToOne @JoinColumn(name = "coachId", insertable = false, updatable = false)
    private Coach coach;
	
}
