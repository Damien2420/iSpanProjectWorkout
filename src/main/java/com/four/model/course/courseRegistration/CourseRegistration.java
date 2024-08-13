package com.four.model.course.courseRegistration;

import com.four.model.course.course.Course;
import com.four.model.memberAdm.MemberBean;

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

@NoArgsConstructor @Getter @Setter @Entity @Table(name = "courseRegistration")
public class CourseRegistration implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer registerID;
	private Integer courseId;
	private Integer memberId;
	private Integer coursePrice;
	private String registerStatus;
	
    @ManyToOne @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private Course course;
    @ManyToOne @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private MemberBean member;
	
}
