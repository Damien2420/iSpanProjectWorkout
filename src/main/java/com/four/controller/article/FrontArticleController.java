package com.four.controller.article;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.four.model.article.Article;
import com.four.model.article.Comment;
import com.four.model.memberAdm.MemberBean;
import com.four.service.article.CommentService;
import com.four.service.article.FrontArticleService;
import com.four.service.memberAdm.MemberService;

import jakarta.servlet.http.HttpSession;


@Controller
public class FrontArticleController {

	@Autowired
	private FrontArticleService frontArticleService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private MemberService memberService;
	
	//查詢全部文章 主頁面
	@GetMapping("/forum/articles")
	public String getByPage(@RequestParam(value="p", defaultValue = "1") Integer pageNumber, Model model) {
		Page<Article> page = frontArticleService.findByPage(pageNumber);
//		Map<String, Integer> tagCount = frontArticleService.getTagCount();
		List<Article> sideArticles = frontArticleService.getSideArticles();
		Integer currentPageNumber = pageNumber;
		Integer totalPageCount = page.getTotalPages();
		model.addAttribute("page", page);
//		model.addAttribute("tagCount", tagCount);
		model.addAttribute("sideArticles", sideArticles);
		model.addAttribute("currentPageNumber", currentPageNumber);
		System.out.println("面前頁數="+currentPageNumber);
		model.addAttribute("totalPageCount", totalPageCount);
		System.out.println("總頁數="+totalPageCount);
		return "article/forumPage";
	}
	
	//點選側邊分類欄位 透過tag查詢
	@GetMapping("/forum/categories")
	public String getPageByCategory(@RequestParam(value="tag", defaultValue="問題") String tag,
									@RequestParam(value="p", defaultValue = "1") Integer pageNumber,
									Model model) {
		Page<Article> page = frontArticleService.getArticleByTag(tag);
		Map<String, Integer> tagCount = frontArticleService.getTagCount();
		List<Article> sideArticles = frontArticleService.getSideArticles();
		System.out.println("目前頁數="+ pageNumber);
		Integer totalPageCount = page.getTotalPages();
		if(totalPageCount == 0) {
			totalPageCount = 1;
		}
		String queryTag = tag;
		System.out.println("其他分類的總頁數="+totalPageCount);
		model.addAttribute("page", page);
		model.addAttribute("tagCount", tagCount);
		model.addAttribute("sideArticles", sideArticles);
		model.addAttribute("currentPageNumber", pageNumber);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("tag", queryTag);
		return "article/forumPage";
	}
	
	//forumArticlePage 頁面 展示單筆文章 及 該文章之留言
	@GetMapping("/forum/article")
	public String getArticleAndComments(@RequestParam(value="articleId", defaultValue = "1") Integer articleId, Model model) {
		Article article = frontArticleService.getArticleById(articleId);
		List<Comment> comments = commentService.findByArticleId(articleId);
		List<Article> sideArticles = frontArticleService.getSideArticles();
		Article previousArticle = frontArticleService.getPreviousArticleById(articleId);
		Article nextArticle = frontArticleService.getNextArticleById(articleId);
		Integer articleAmount = frontArticleService.getArticleAmount();
		model.addAttribute("articleAmount", articleAmount);
		model.addAttribute("article", article);
		model.addAttribute("comments", comments);
		model.addAttribute("sideArticles", sideArticles);
		if(nextArticle != null) {
			model.addAttribute("nextArticle", nextArticle);
		}
		if(previousArticle != null) {
			model.addAttribute("previousArticle", previousArticle);
		}
		return "article/forumArticlePage";
	}
	
	//跳轉頁面到文字編輯器
	@GetMapping("/forum/texteditor")
	public String texteditor() {
		return "article/texteditor";
	}
	
	//新增文章
	@PostMapping("/forum/insertArticle")
	public String insertArticle(@RequestParam("articleTitle") String articleTitle, 
								@RequestParam("category") String category,
								@RequestParam("articleContent") String articleContent,
								@RequestParam("postTime") String postTime,
								HttpSession session
			) {
		MemberBean member = (MemberBean) session.getAttribute("user");
		
		System.out.println("memberid: " + member);
//		System.out.println("memberid: " + member.getMemNo());//null
		
		frontArticleService.insertArticle(articleContent, category, articleTitle, postTime, member);
		return "redirect:/forum/articles";
	}
	
	//取得特定會員的所有文章(前台會員中心檢視)
	@GetMapping("/forum/memcenterArticles")
	public String getArticlesByMemberId(HttpSession session, Model model) {
		MemberBean member = (MemberBean) session.getAttribute("user");

		List<Article> articles = frontArticleService.getArticlesByMemberId(member);
		model.addAttribute("articles", articles);
		model.addAttribute("member", member);
		return "/article/memcenterArticlePage";
	}
	
	//跳轉頁面
	@PostMapping("/forum/editArticlePage")
	public String editCommentPage(@RequestParam("id") Integer articleId, Model model) {
		Article article = frontArticleService.findByArticleId(articleId);
		model.addAttribute("article", article);
		model.addAttribute("member", article.getMemberId());
		return "article/editArticlePage";
	}
	
	//會員中心 編輯發文
	@PostMapping("/forum/editArticle")
	public String editComment(@RequestParam("articleId") Integer articleId,
								@RequestParam("articleContent") String articleContent,
								@RequestParam("postTime") String postTime,
								@RequestParam("tag") String tag, 
								@RequestParam("articleTitle") String articleTitle) {
		Article article = frontArticleService.findByArticleId(articleId);
		article.setArticleContent(articleContent);
		article.setPostTime(postTime);
		article.setTag(tag);
		article.setArticleTitle(articleTitle);
		frontArticleService.saveArticle(article);
		return "redirect:/forum/memcenterArticles";
	}
		
	//會員中心 刪除文章
	@PostMapping("/forum/deleteArticle")
	public String deleteArticle(@RequestParam("id") Integer articleId) {
		Article article = frontArticleService.findByArticleId(articleId);
		article.setArticleDisplay("0");
		frontArticleService.saveArticle(article);
		return "redirect:/forum/memcenterArticles";
	}
	
	
	
	
	
	
	
}
