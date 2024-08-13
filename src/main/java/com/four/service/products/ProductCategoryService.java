package com.four.service.products;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.products.ProductCategory;
import com.four.model.products.ProductCategoryRepository;

@Service
@Transactional
public class ProductCategoryService {
	
	@Autowired
	private ProductCategoryRepository categoryRepository;

	public List<ProductCategory> searchAllCategories() {
		return categoryRepository.findAll();
	}
	
	public ProductCategory singleSearchCategory(int categoryID) {
		Optional<ProductCategory> result = categoryRepository.findById(categoryID);
		return result.isPresent() ? result.get() : null;
	}

	public ProductCategory insertCategory(ProductCategory newCategory) {
		return categoryRepository.save(newCategory);
	}

	

}
