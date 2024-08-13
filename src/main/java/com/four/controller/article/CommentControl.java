package com.four.controller.article;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.four.model.article.Comment;
import com.four.model.memberAdm.MemberBean;
import com.four.service.article.CommentService;
import com.four.service.memberAdm.MemberService;

import jakarta.servlet.http.HttpSession;



@Controller
public class CommentControl {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private MemberService memberService;
	
	//查詢特定文章的所有留言
	@GetMapping("/forum/comment")
	public List<Comment> getCommentsByArticleId(@RequestParam(value="articleId") Integer articleId) {
		return commentService.findByArticleId(articleId);
	}
	
	//新增一則留言
	@PostMapping("/forum/comment")
	public String saveComment(@RequestParam("commentContent") String commentContent,
							@RequestParam("articleId") Integer articleId,
							@RequestParam("postTime") String postTime,
							HttpSession session,
							RedirectAttributes redirectAttributes) {
		MemberBean member = (MemberBean) session.getAttribute("user");
		commentService.saveComment(commentContent, articleId, postTime, member);
		redirectAttributes.addAttribute("articleId", articleId);
		return "redirect:/forum/article";
	}
	
	//查所有comment (後台)
	@GetMapping("/admin/forum/getAllComments")
	public String getAllComments(Model model) {
		List<Comment> results = commentService.getAllComments();
		model.addAttribute("comments", results);
		return "article/showCommentPage";
	}
	
	//查特定會員所有留言 (前台會員中心檢視)
	@GetMapping("/forum/memcenterComments")
	public String getMethodName(HttpSession session, Model model) {
		MemberBean member = (MemberBean) session.getAttribute("user");
		List<Comment> comments = commentService.getCommentsByMemberId(member);
		model.addAttribute("comments", comments);
		model.addAttribute("member", member);
		return "article/memcenterCommentPage";
	}
	
	//更新狀態(切換成相反狀態)
	@PutMapping("/article/updateStatusComment")
	public String updateStatusComment(@RequestParam("commentId") Integer commentId) {
		Comment comment = commentService.findByCommentId(commentId);
		Comment commentAdjust = commentService.updateCommentStatus(comment);
		commentService.saveComment(commentAdjust);
		return "article/showCommentPage";
	}
	
	//跳轉頁面
	@PostMapping("/forum/editCommentPage")
	public String editCommentPage(@RequestParam("id") Integer commentId, Model model) {
		Comment comment = commentService.findByCommentId(commentId);
		model.addAttribute("comment", comment);
		return "article/editCommentPage";
	}
	
	//會員中心 編輯留言
	@PostMapping("/forum/editComment")
	public String editComment(@RequestParam("commentId") Integer commentId,
								@RequestParam("commentContent") String commentContent,
								@RequestParam("postTime") String postTime) {
		Comment comment = commentService.findByCommentId(commentId);
		comment.setCommentContent(commentContent);
		comment.setCommentTime(postTime);
		System.out.println("postTime="+postTime);
		commentService.saveComment(comment);
		return "redirect:/forum/memcenterComments";
	}
	
	//會員中心 刪除留言
	@PostMapping("/comment/deleteComment")
	public String deleteComment(@RequestParam("id") Integer commentId) {
		Comment comment = commentService.findByCommentId(commentId);
		comment.setCommentDisplay(0);
		Integer commentAmount = comment.getArticleId().getCommentCount();
		commentAmount -= 1;
		comment.getArticleId().setCommentCount(commentAmount);
		commentService.saveComment(comment);
		return "redirect:/forum/memcenterComments";
	}
	
	
	
	
	
	
}
