package com.four.service.article;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.four.model.article.Article;
import com.four.model.article.ArticleRepository;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepo;
	
	public List<Article> getAllArticles() {
		List<Article> articles = articleRepo.findAll();
		return articles;
	}
	
	public Article saveArticle(Article article) {
		return articleRepo.save(article);
	}
	
	public Article getArticleById(Integer articleId) {
		Optional<Article> optional = articleRepo.findById(articleId);
		if(optional.isEmpty()) {
			return null;
		}
		return optional.get();
	}
	
	public void deleteArticleById(Integer id) {
		articleRepo.deleteById(id);
	}
	
	public List<Article> getArticlesByTitle(String Title){
		return articleRepo.getArticlesByTitle(Title);
	}
	
	public Page<Article> findByPage(Integer pageNumber){
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.ASC, "articleId");
		Page<Article> page = articleRepo.findAll(pageable);
		return page;
	}
	
	public Page<Article> findByPageAndFuzzyQuery(Integer pageNumber, String Title){
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.ASC, "articleId");
		List<Article> articles = articleRepo.getArticlesByTitle(Title);
		
		int start = (int) pageable.getOffset();
	    int end = (start + pageable.getPageSize()) > articles.size() ? articles.size() : (start + pageable.getPageSize());
	    Page<Article> page = new PageImpl<>(articles.subList(start, end), pageable, articles.size());
		
		return page;
	}
	
	//後臺調整文章是否顯示
	public Article updateArticleStatus(Article article) {
		if(article.getArticleDisplay().equals("1")) {
			article.setArticleDisplay("0");
		}else {
			article.setArticleDisplay("1");
		}
		return article;
	}
	
	
	
	
}
