package com.four.service.products;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.products.Product;
import com.four.model.products.ProductRepository;
import com.four.utils.CartDTO;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	public Product singleSearchProduct(int id) {
		Optional<Product> result = productRepository.findById(id);
		Product resultProduct =  result.isPresent() ? result.get() : null;
		Hibernate.initialize(resultProduct.getProductImage());
		return resultProduct;
	}
	
	public Product singleSearchProductWithoutImage(int id) {
		Optional<Product> result = productRepository.findById(id);
		Product resultProduct =  result.isPresent() ? result.get() : null;
		return resultProduct;
	}
	
	public Page<Product> searchAllProductByPage(int pageNumber){
		Pageable page = PageRequest.of(pageNumber - 1, 5, Sort.Direction.ASC, "productID");
		Page<Product> result = productRepository.findAll(page);
		return result;
	}
	
	public Page<Product> mallsearchLaunchedProductsByPage(int pageNumber) {
		Pageable page = PageRequest.of(pageNumber - 1, 9, Sort.Direction.ASC, "productID");
		return productRepository.findByLaunchStatus(true, page);
	}
	
	
	public Page<Product> findCategoryProductsByPage(Integer categoryID, int pageNumber) {
		Pageable page = PageRequest.of(pageNumber - 1, 9, Sort.Direction.ASC, "productID");
		return productRepository.findByProductCategoryID(categoryID, page);
	}

	public Product insertProduct(Product product) {
		 return productRepository.save(product);
	}

	public Product updateProduct(Product newProduct) {
		return productRepository.save(newProduct);
	}

	public boolean deleteProducts(List<Integer> idList) {
		int affectedRows = idList.size();
		for(Integer id : idList) {
			productRepository.deleteById(id);
			affectedRows--;
		}
		return affectedRows == 0 ? true : false;
	}

	public List<Product> fuzzySearchProductsByCategoryAndName(String keyword) {
		return productRepository.fuzzySearchNameAndCategory(keyword);
	}
	
	public Page<Product> fuzzySearchByProductname(String keyword, int pageNumber) {
		Pageable page = PageRequest.of(pageNumber - 1, 9, Sort.Direction.ASC, "productID");
		return productRepository.fuzzySearchProductName(keyword, page);
	}
	
	public List<CartDTO> updateCart(List<CartDTO> cart, Integer targetID, Integer quantity, String action) {
	    Iterator<CartDTO> iterator = cart.iterator();
	    while (iterator.hasNext()) {
	        CartDTO item = iterator.next();
	        Integer id = item.getProduct().getProductID();
	        if (id.equals(targetID)) {
	            switch (action) {
	                case "add":
	                    item.setQuantity(item.getQuantity() + 1);
	                    break;
	                case "minus":
	                    if (item.getQuantity() == 1) {
	                        iterator.remove();
	                    } else {
	                        item.setQuantity(item.getQuantity() - 1);
	                    }
	                    break;
	                case "change":
	                    item.setQuantity(quantity);
	                    break;
	            }
	            break;
	        }
	    }
	    return cart;
	}

	public List<Product> randomFourProducts() {
		List<Product> resultList = new ArrayList<>();
		Random random = new Random();
		int productCounts = productRepository.countProducts();
		if(productCounts < 4) {
			return null;
		}
		Set<Integer> uniqueNumbers = new HashSet<>();
		while(resultList.size() < 4) {
			while(uniqueNumbers.size() < 4) {
				int randomTarget = random.nextInt(productCounts);
				uniqueNumbers.add(randomTarget);
			}
			
			uniqueNumbers.forEach( number -> {
				Pageable page = PageRequest.of(number, 1, Sort.Direction.DESC, "productID");
				Product productFound = productRepository.findByLaunchStatus(true, page).getContent().get(0);
				resultList.add(productFound);
			});
		}
	
		return resultList;
	}

}
