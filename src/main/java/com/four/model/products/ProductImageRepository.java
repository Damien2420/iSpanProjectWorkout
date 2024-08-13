package com.four.model.products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

	@Query("SELECT i FROM ProductImage i WHERE i.imageSequence = 1")
	List<ProductImage> findByImageSequence();
	
	@Query("SELECT i FROM ProductImage i WHERE i.product.productID = :productID AND imageSequence = 1")
	ProductImage findFirstImagebyProductId(@Param("productID")Integer productID);
	
	@Query("SELECT i.imageSequence FROM ProductImage i WHERE i.product.productID = :targetProduct ORDER BY i.imageSequence DESC")
	Integer getMaxImageSequenceNumber(@Param("targetProduct")Integer productID);
	
	@Query("SELECT i FROM ProductImage i WHERE i.product.productID = :target AND i.imageSequence > :sequence")
	List<ProductImage> findUpdateImagesByProductID(@Param("target") Integer productID, @Param("sequence") Integer imageSequence);
}
