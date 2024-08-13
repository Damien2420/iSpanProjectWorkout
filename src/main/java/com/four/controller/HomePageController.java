package com.four.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.four.model.article.Article;
import com.four.model.products.Product;
import com.four.service.article.FrontArticleService;
import com.four.service.products.ProductService;

@Controller
public class HomePageController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private FrontArticleService articleService;
	
	//首頁
	@GetMapping("/home")
	public String homePage(Model model) {
		List<Product> recommend = productService.randomFourProducts();
		List<Article> sideArticles = articleService.getSideArticles();
		model.addAttribute("recommend", recommend);
		model.addAttribute("sideArticles", sideArticles);
		return "front/index";
	}
	
	//後台首頁
	@GetMapping("/admin/index")
	public String indexPage() {
		return "back/index";
	}
	
	@GetMapping("/contacts")
	public String contactsPage() {
		return "/front/contacts";
	}
}
