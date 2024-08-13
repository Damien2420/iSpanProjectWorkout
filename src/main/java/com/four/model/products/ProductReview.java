package com.four.model.products;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.four.model.memberAdm.MemberBean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "productreview")
@Getter @Setter
@NoArgsConstructor
public class ProductReview {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reviewID;
	private Integer rating;
	
	@JsonFormat(timezone = "GMT-8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate reviewDate;
	
	@ManyToOne @JoinColumn(name = "productID")
	private Product product;
	
	@ManyToOne @JoinColumn(name = "memberID")
	private MemberBean member;
	
	public ProductReview(Integer rating, LocalDate reviewDate, Product product, MemberBean member) {
		this.rating = rating;
		this.reviewDate = reviewDate;
		this.product = product;
		this.member = member;
	}
	
	@PrePersist
	public void createReviewTime() {
		if(reviewDate == null) {
			reviewDate = LocalDate.now();
		}
	}
}
