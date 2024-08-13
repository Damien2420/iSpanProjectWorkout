package com.four.model.products;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("FROM Product p JOIN p.productCategory pc WHERE p.productName LIKE %:keyword% OR pc.categoryName LIKE %:keyword%")
	List<Product> fuzzySearchNameAndCategory(@Param("keyword") String keyword);

	@Query("FROM Product p WHERE p.productName LIKE %:keyword%")
	Page<Product> fuzzySearchProductName(@Param("keyword") String keyword, Pageable page);
	
	@Query("FROM Product p WHERE p.productCategory.categoryID = :id AND p.launchStatus = true")
	Page<Product> findByProductCategoryID(@Param("id") int categoryID, Pageable page);
	
	@Query("SELECT COUNT(p) FROM Product p WHERE p.launchStatus = true")
	int countProducts();
	
	Page<Product> findByLaunchStatus(Boolean launchStatus, Pageable page);
}
