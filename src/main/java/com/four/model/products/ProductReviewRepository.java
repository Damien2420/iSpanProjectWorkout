package com.four.model.products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {

	List<ProductReview> findByProduct(Product product);
    
	@Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.productID = :id")
	Double averageRatingByProductID(@Param("id") Integer productID);
    
    @Query(value = "SELECT rating, " +
            "FLOOR((COUNT(*) * 1.0 / SUM(COUNT(*)) OVER ()) * 100) AS rating_percentage " +
            "FROM productreview " +
            "WHERE productID = :id " +
            "GROUP BY rating " +
            "ORDER BY rating", 
            nativeQuery = true)
    List<Object[]> findRatingPercentageByProductId(@Param("id") int productId);
}
