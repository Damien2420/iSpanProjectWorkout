package com.four.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.four.model.products.Product;
import com.four.model.products.ProductImage;
import com.four.service.products.ProductImageService;
import com.four.service.products.ProductService;

@Component
public class ProductInit implements ApplicationRunner {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductImageService imageService;
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!imageService.check()) {
			try {
				List<ProductImage> allImages = createImageBean();
				allImages.forEach(bean -> imageService.insertImage(bean));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<ProductImage> createImageBean() throws IOException {
		List<ProductImage> imageBeans = new ArrayList<>();
		Resource belt = resourceLoader.getResource("classpath:static/images/products/belt.webp");
		Resource watch = resourceLoader.getResource("classpath:static/images/products/watch.jpg");
		Resource waterbottle = resourceLoader.getResource("classpath:static/images/products/waterbottle.jpg");
		
		List<Resource> images = Arrays.asList(belt, watch, waterbottle);
		int imageSequence = 1, productID = 101;
		for (Resource image : images) {
			String fileName = image.getFilename();
			byte[] imageInByte = getImageByteArray(image);
			Product resultProduct = productService.singleSearchProduct(productID);
			imageBeans.add(new ProductImage(fileName, imageSequence, imageInByte, resultProduct));
			productID++;
		}
		return imageBeans;
	}

	private byte[] getImageByteArray(Resource image) throws IOException {
		try (InputStream input = new BufferedInputStream(image.getInputStream());
			 ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buffer)) != -1) {
				byteArray.write(buffer, 0, bytesRead);
			}
			return byteArray.toByteArray();
		}
	}
}
