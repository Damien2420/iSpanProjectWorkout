package com.four.controller.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.article.Article;
import com.four.model.article.Comment;
import com.four.model.article.Report;
import com.four.service.article.CommentService;
import com.four.service.article.FrontArticleService;
import com.four.service.article.ReportService;

@Controller
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private FrontArticleService frontArticleService;

	//後台檢舉畫面 分成已受理 未受理
	@GetMapping("/admin/forum/getAllReports")
	public String getReports(Model model) {
		List<Report> pendingReports = reportService.getPendingReports();
		List<Report> processedReports = reportService.getProcessedReports();
		model.addAttribute("processedReports", processedReports);
		model.addAttribute("pendingReports", pendingReports);
		return "article/showReportPage";
	}
	
	//ajax請求 已處理的檢舉列表
//	@GetMapping("/forum/report")
//	public Page<Report> getProcessedReports(@RequestParam(value = "verdict") Integer verdict, Integer pageNumber){
//		pageNumber = 1;
//		Page<Report> pages = reportService.findProdessedRepByPage(pageNumber, verdict);
//		return pages;
//	}

	//按下展開按鈕的ajax請求 
	@GetMapping("/forum/report/{reportId}")
	@ResponseBody
	public Report getReportById(@PathVariable Integer reportId) {
        Report report = reportService.getReportById(reportId);
        return report;
    }
	
	//檢舉受理或駁回按鈕的ajax
	@ResponseBody
	@PutMapping("/forum/report/{reportId}")
	public ResponseEntity<Void> updateReport(@PathVariable Integer reportId, @RequestBody Integer verdict, Model model) {
		Report report = reportService.handleReport(reportId, verdict);
		System.out.println("reportid= " + report.getReportId());
		return ResponseEntity.ok().build();
	}
	
	
	//前台新增留言的檢舉
	@PostMapping("/forum/insertCommentReport")
	public String insertCommentReport(@RequestParam("reportType") String reportType, 
									@RequestParam("reportDescribe") String reportDescribe, 
									@RequestParam("commentId") Integer commentId,
									@RequestParam("articleId" ) Integer articleId){

		Comment comment = commentService.findByCommentId(commentId);
		reportService.saveCommentReport(reportType, reportDescribe, comment);
		
		return "redirect:/forum/article?articleId=" + articleId;
	}
	
	//前台新增文章的檢舉
	@PostMapping("/forum/insertArticleReport")
	public String insertArticleReport(@RequestParam("reportType") String reportType,
										@RequestParam("reportDescribe") String reportDescribe,
										@RequestParam("articleId" ) Integer articleId){
		
		Article article = frontArticleService.getArticleById(articleId);
		reportService.saveArticleReport(reportType, reportDescribe, article);
		
		return "redirect:/forum/article?articleId=" + articleId;
	}
	
	
	
	
	
	

}
