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

@Entity
@Table(name = "article")
@Getter @Setter @NoArgsConstructor
public class Article {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer articleId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId", referencedColumnName = "memNo")
	private MemberBean memberId;
	
	@Column(nullable = false, length = 50)
	private String postTime;
	
	@Column(nullable = false, length = 10)
	private String tag;
	
	@Column(nullable = false, length = 50)
	private String articleTitle;
	
	@Column(nullable = false, length = 2000)
	private String articleContent;
	
	@Column(nullable = false, length = 5)
	private String articleDisplay;
	
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer likeAmount;
	
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer commentCount;

	
}
