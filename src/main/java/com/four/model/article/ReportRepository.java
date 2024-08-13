package com.four.model.article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer>{
	
	Page<Report> findByVerdict(Integer verdict, Pageable pageable);
	
	List<Report> findByReportState(Integer reportState);
	
	
}
