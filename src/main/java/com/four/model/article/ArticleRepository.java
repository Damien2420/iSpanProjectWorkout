package com.four.model.article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.four.model.memberAdm.MemberBean;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query("FROM Article WHERE articleTitle LIKE %:articleTitle%")
	List<Article> getArticlesByTitle(@Param(value = "articleTitle") String title);
	
	//取得各分類的文章數
	@Query("SELECT COUNT(*) FROM Article WHERE tag = :tag")
	Integer getTagCount(@Param(value="tag") String tag);
	
	//用留言數查詢
	@Query("SELECT a FROM Article a WHERE a.articleDisplay = :articleDisplay ORDER BY a.commentCount DESC")
	List<Article> findArticlesByDisplayOrderByCommentCountDesc(@Param("articleDisplay") String articleDisplay);
	
	//用標籤查文章 去掉被隱藏(刪除)的文章
    Page<Article> findByTagAndArticleDisplay(String tag, String articleDisplay, Pageable pageable);

    //取文章總數
    @Query("SELECT COUNT(a) FROM Article a")
    Integer getArticleAmount();
    
    //查特定會員的所有發文 去掉被隱藏(刪除)的文章
    List<Article> findByMemberIdAndArticleDisplay(MemberBean memberId, String articleDisplay);
    
    //查詢所有文章 去掉被隱藏(刪除)的文章
    Page<Article> findByArticleDisplay(String articleDisplay, Pageable pageable);

}
