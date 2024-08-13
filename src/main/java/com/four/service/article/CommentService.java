package com.four.service.article;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.four.model.article.Article;
import com.four.model.article.ArticleRepository;
import com.four.model.article.Comment;
import com.four.model.article.CommentRepository;
import com.four.model.memberAdm.MemberBean;
import com.four.model.memberAdm.MemberRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private ArticleRepository articleRepo;
	
	@Autowired
	private MemberRepository memberRepo;
	
	//查詢特定文章的所有留言
	public List<Comment> findByArticleId(Integer articleId){
		Optional<Article> optional = articleRepo.findById(articleId);
		Article article = new Article();
		if(optional.isEmpty()) {
		}else {
			article = optional.get();
		}
		return commentRepo.findByArticleIdAndCommentDisplay(article, 1);
		
	}
	
	public Comment findByCommentId(Integer commentId) {
		Optional<Comment> optional = commentRepo.findById(commentId);
		if(optional.isEmpty()) {
			return null;
		}
		else {
			return optional.get();
		}
	}
	
	//新增一則留言
	@SuppressWarnings("deprecation")
	public void saveComment(String commentContent, Integer articleId, String postTime, MemberBean member) {
		Comment comment = new Comment();
		
		System.out.println("commentContent = "+ commentContent);
		System.out.println("articleId = "+ articleId);
		System.out.println("postTime = "+ postTime);
		
		comment.setCommentContent(commentContent);
		Article article = articleRepo.getById(articleId);
		comment.setArticleId(article);
		if(member != null) {
			comment.setMemberId(member);
		}else {
			//如果member是null 給直接給會員10000001的資料
			MemberBean memberTemp = new MemberBean();
			Optional<MemberBean> optional = memberRepo.findById(10000001);
			if(optional.isEmpty()) {
				System.out.println("找不到會員 這個留言沒有暱稱 " + commentContent);
			}else {
				memberTemp = optional.get();
				comment.setMemberId(memberTemp);
			}
		}
		comment.setCommentDisplay(1);
		comment.setCommentTime(postTime);
		Comment successComment = commentRepo.save(comment);
		if(successComment != null) {
			Integer commentAmount = article.getCommentCount();
			commentAmount += 1;
			article.setCommentCount(commentAmount);
			articleRepo.save(article);
		}
	}
	
	//取全部的comment
	public List<Comment> getAllComments(){
		List<Comment> comments = commentRepo.findAll();
		return comments;
	}
	
	//取得特定會員的所有留言
	public List<Comment> getCommentsByMemberId(MemberBean memberId){
		return commentRepo.findByMemberIdAndCommentDisplay(memberId, 1);
	}
		
	//儲存一筆
	public Comment saveComment(Comment comment) {
		return commentRepo.save(comment);
	}
	
	//後臺調整留言是否顯示
	public Comment updateCommentStatus(Comment comment) {
		if(comment.getCommentDisplay() == 1) {
			comment.setCommentDisplay(0);
			//對留言數量做增減
			Integer commentAmount = comment.getArticleId().getCommentCount();
			commentAmount -= 1;
			comment.getArticleId().setCommentCount(commentAmount);
		}else {
			comment.setCommentDisplay(1);
			Integer commentAmount = comment.getArticleId().getCommentCount();
			commentAmount += 1;
			comment.getArticleId().setCommentCount(commentAmount);
		}
		return comment;
	}
	
	
	
}
