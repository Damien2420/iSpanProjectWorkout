package com.four.model.article;


import com.four.model.memberAdm.AdminBean;
import com.four.model.memberAdm.MemberBean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@Getter @Setter @NoArgsConstructor
public class Report {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reportId;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "memberId", referencedColumnName = "memNo", nullable = false)
//	private MemberBean memberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "articleId")
	private Article articleId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commentId")
	private Comment commentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "whistleblowerMemId", referencedColumnName = "memNo")
	private MemberBean whistleblowerMemId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "whistleblowerAdmId", referencedColumnName = "admNo")
	private AdminBean whistleblowerAdmId;
	
	@Column(nullable = false, length = 20)
	private String reportType;
	
	@Column(nullable = false, length = 200)
	private String reportDescribe;

	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer reportState;
	
	@Column(columnDefinition = "int default 0")
	private Integer verdict;
	
	
	
	
	
	
	
	
	
	
	
	
}
