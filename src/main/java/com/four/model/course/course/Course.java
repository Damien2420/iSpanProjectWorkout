package com.four.model.course.course;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.four.model.coach.coach.Coach;
import com.four.model.course.schedule.Schedule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter @Entity @Table(name = "course")
public class Course implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer courseId;
	
	private Integer coachId;
	
	private String courseName;
	
	private String courseProfile;

    private String courseType;
    
    private Integer coursePrice;

    private Date courseStartDate;
    
    private Date courseEndDate;
    
    private String courseStatus;
    
    @ManyToOne @JoinColumn(name = "coachId", insertable = false, updatable = false)
    private Coach coach;
    
    @JsonIgnore @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Schedule> schedules = new ArrayList<>();
	
}