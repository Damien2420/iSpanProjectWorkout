package com.four.model.course.course;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.four.model.coach.coach.Coach;
import com.four.model.course.schedule.Schedule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class CourseDTO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name = "courseId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer  courseId;
	
	@Column(name = "courseName")
	private String courseName;
	
	@Column(name = "coursePrice")
    private Integer coursePrice;
	
	@Column(name = "courseStartDate")
    private Date courseStartDate;
    
	@Column(name = "courseEndDate")
    private Date courseEndDate;
	
    @OneToOne @JoinColumn(name = "coachId", insertable = false, updatable = false)
    private Coach coach;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Schedule> schedules = new ArrayList<>();
	
}