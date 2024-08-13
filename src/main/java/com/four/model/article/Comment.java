package com.four.model.article;

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

@Entity @Table(name = "comment")
@Getter @Setter @NoArgsConstructor
public class Comment {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId", referencedColumnName = "memNo")
	private MemberBean memberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "articleId")
	private Article articleId;
	
	@Column(nullable = false, length = 200)
	private String commentContent;
	
	@Column(nullable = false)
	private String commentTime;
	
	@Column(nullable = false)
	private Integer commentDisplay;
	
	
	
	
	
	
	

}
