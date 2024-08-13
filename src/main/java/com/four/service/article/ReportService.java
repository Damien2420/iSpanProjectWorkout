package com.four.service.article;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.four.model.article.Article;
import com.four.model.article.ArticleRepository;
import com.four.model.article.Comment;
import com.four.model.article.CommentRepository;
import com.four.model.article.Report;
import com.four.model.article.ReportRepository;

@Service
public class ReportService {

	@Autowired
	private ReportRepository reportRepo;
	
	@Autowired
	private ArticleRepository articleRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	//新增一筆留言的檢舉
	public void saveCommentReport(String reportType,String reportDescribe,Comment comment) {
		Report report = new Report();
		report.setReportType(reportType);
		report.setReportDescribe(reportDescribe);
		report.setCommentId(comment);
		report.setReportState(0);
		report.setVerdict(0);
		
		reportRepo.save(report);
	}
	
	//新增一筆文章的檢舉
	public void saveArticleReport(String reportType, String reportDescribe, Article article) {
		Report report = new Report();
		report.setReportType(reportType);
		report.setReportDescribe(reportDescribe);
		report.setArticleId(article);
		report.setReportState(0);
		report.setVerdict(0);
		
		reportRepo.save(report);
	}
	
	
	public Report getReportById(Integer id) {
		Optional<Report>optional = reportRepo.findById(id);
		if(optional.isEmpty()) {
			return null;
		}
		return optional.get();
	}
	
//	public Page<Report> findByPage(Integer pageNumber){
//		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.ASC, "reportId");
//		Page<Report> page = reportRepo.findAll(pageable);
//		return page;
//	}
//	
//	public Page<Report> findProdessedRepByPage(Integer pageNumber, Integer verdict){
//		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.ASC, "reportId");
//		Page<Report> page = reportRepo.findByVerdict(verdict, pageable);
//		return page;
//	}
	
	public void rejectReport(Integer reportId) {
		Optional<Report> optional = reportRepo.findById(reportId);
		if(optional.isPresent()) {
			Report report = optional.get();
			report.setReportState(1);
			report.setVerdict(1);
			reportRepo.save(report);
		}
	}
	
	public Report handleReport(Integer reportId, Integer verdict) {
		Optional<Report> optional = reportRepo.findById(reportId);
		Integer handleReportDone = 1;
		if(optional.isPresent()) {
			Report report = optional.get();
			report.setReportState(handleReportDone);
			report.setVerdict(verdict);
			reportRepo.save(report);
			
			//修改article comment的display狀態(假刪除)
			if(verdict == 2) {
				if (report.getArticleId() != null) {
					Optional<Article> optionalArticle = articleRepo.findById(report.getArticleId().getArticleId());
					if (optionalArticle.isPresent()) {
						Article article = optionalArticle.get();
						article.setArticleDisplay("0");
						articleRepo.saveAndFlush(article);
					}
				} else {// report.getCommentId()!= null
					Optional<Comment> optionalComment = commentRepo.findById(report.getCommentId().getCommentId());
					if (optionalComment.isPresent()) {
						Comment comment = optionalComment.get();
						comment.setCommentDisplay(0);
						Integer commentCount = comment.getArticleId().getCommentCount();
						commentCount -=1;
						comment.getArticleId().setCommentCount(commentCount);
						commentRepo.saveAndFlush(comment);
					}
				}
			}
			return report;
		}
		return null;
	}
	
	//取的所有未處理檢舉
	public List<Report> getPendingReports(){
		return reportRepo.findByReportState(0);
	}
	
	//取得所有已處理檢舉
	public List<Report> getProcessedReports(){
		return reportRepo.findByReportState(1);
	}
	
	
}
