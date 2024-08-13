package com.four.model.course.schedule;

import com.four.model.course.course.Course;

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

@NoArgsConstructor @Getter @Setter @Entity @Table(name = "schedule")
public class Schedule implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer scheduleId;
	
	private String weekday;
	
	private String daytime;
	
	private Integer courseId;
    
    @ManyToOne @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private Course course;

}