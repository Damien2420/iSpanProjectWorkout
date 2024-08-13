package com.four.service.products;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.four.model.products.Product;
import com.four.model.products.ProductReview;
import com.four.model.products.ProductReviewRepository;

@Service
public class ProductReviewService {
	
	@Autowired
	private ProductReviewRepository reviewRepository;
	@Autowired
	private ProductService productService;
	
	//取得所有評價
	public List<ProductReview> findAllReviewByProduct(int productID) {
		Product target = productService.singleSearchProductWithoutImage(productID);
		return reviewRepository.findByProduct(target);
	}
	
	//取得所有評價分類小計
	public Map<String, Integer> allReviewPercentagesByProductID(int productID) {
		List<Object[]> searchResult = reviewRepository.findRatingPercentageByProductId(productID);
		Map<String, Integer> ratingLevels = new HashMap<>();
		for(Object[] data : searchResult) {
			ratingLevels.put(data[0].toString(), ((BigDecimal) data[1]).intValue());
		}
		return ratingLevels;
	}
	
	//取得所有評價數量與平均評價
	public Double getAllRatingCountsAndAverageRatings(int productID) {
		return reviewRepository.averageRatingByProductID(productID);
	}
	
	public ProductReview saveReview(ProductReview review) {
		return reviewRepository.save(review);
	}
}
