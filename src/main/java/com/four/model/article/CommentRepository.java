package com.four.model.article;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.four.model.memberAdm.MemberBean;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
//	@Query("SELECT COUNT(*) FROM Comment WHERE articleId = :articleId")
//    Integer getCommentCountByArticleId(@Param(value = "articleId") Integer articleId);
	
	//用articleId找目標文章的所有留言
	List<Comment> findByArticleIdAndCommentDisplay(Article article, Integer commentDisplay);
	
	//查特定會員的所有留言
	List<Comment> findByMemberIdAndCommentDisplay(MemberBean memberId, Integer commentDisplay);
}
