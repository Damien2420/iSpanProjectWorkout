package com.four.controller.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.four.model.article.Article;
import com.four.service.article.ArticleService;
import org.springframework.web.bind.annotation.PutMapping;




@Controller
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;

	//後台所有文章頁面(datatable)
	@GetMapping("/admin/forum/getAllArticles")
	public String findAllArticles(Model model) {
		List<Article> results = articleService.getAllArticles();
		model.addAttribute("articles", results);
		return "article/showArticlePage";
	}
	//不帶分頁的模糊查詢
//	@GetMapping("/article/getArticlesByTitle")
//	public String getArticlesByTitle(@RequestParam String title, Model model) {
//		List<Article> results = articleService.getArticlesByTitle(title);
//		model.addAttribute("articles", results);
//		return "article/allArticlesPage";
//	}
	
	@GetMapping("/article/getArticleById")
	public String getArticlesById(@RequestParam Integer articleId, Model model) {
		Article result = articleService.getArticleById(articleId);
		model.addAttribute("article",result);
		return "article/updateArticlePage";
	}
	
	@GetMapping("/article/insertArticlePage")
	public String insertArticlePage() {
		return "article/insertArticlePage";
	}
	
	@PostMapping("/article/insertArticle")
	public String insertArticle(Article article) {
		@SuppressWarnings("unused")
		Article resultArticle = articleService.saveArticle(article);
		return "redirect:/article/getAllArticles";
	}
	
	@DeleteMapping("/article/deleteArticle")
	public String deleteArticle(@RequestParam Integer articleId) {
		articleService.deleteArticleById(articleId);
		return "redirect:/article/getAllArticles";
	}
	
	@PutMapping("/article/updateArticle")
	public String updateArticle(@ModelAttribute Article article) {
		@SuppressWarnings("unused")
		Article resultArticle = articleService.saveArticle(article);
		return "redirect:/article/getAllArticles";
	}
	
	@GetMapping("/article/getAllArticles")
	public String getByPage(@RequestParam(value="p", defaultValue = "1") Integer pageNumber, Model model) {
		Page<Article> page = articleService.findByPage(pageNumber);
		model.addAttribute("page", page);
		return "article/showArticlePage";
	}
	
	@GetMapping("/article/getArticlesByTitle")
	public String getArticlesByTitle(@RequestParam(value="p", defaultValue = "1") Integer pageNumber,@RequestParam(value="title") String title, Model model) {
		System.out.println("pagenumber= "+ pageNumber);
		Page<Article> page = articleService.findByPageAndFuzzyQuery(pageNumber, title);
		model.addAttribute("page", page);
		model.addAttribute("title", title);
		System.out.println("pagenumber= "+ pageNumber);
		return "article/allArticlesByFuzzyQueryPage";
	}
	
	//更新狀態(切換成相反狀態)
	@PutMapping("/article/updateStatus")
	public String updateArticleStatus(@RequestParam("articleId") Integer articleId) {
		System.out.println("有進到updateStatus");
		Article article = articleService.getArticleById(articleId);
		System.out.println("aaa"+article.getArticleDisplay());
		Article articleAdjust = articleService.updateArticleStatus(article);
		System.out.println("bbb"+article.getArticleDisplay());
		
		articleService.saveArticle(articleAdjust);
		return "article/showArticlePage";
	}
	
	
	
}
