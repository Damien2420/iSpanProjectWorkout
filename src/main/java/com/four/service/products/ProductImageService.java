package com.four.service.products;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.products.ProductImage;
import com.four.model.products.ProductImageRepository;

@Service
@Transactional
public class ProductImageService {
	
	@Autowired
	private ProductImageRepository imageRepository;

	
	public ProductImage findFirstImageByProductID(int selectedID) {
		return imageRepository.findFirstImagebyProductId(selectedID);
	}
	
	public ProductImage findImageById(Integer imageId) {
		 Optional<ProductImage> result = imageRepository.findById(imageId);
		 if(result.isEmpty()) {
			 return null;
		 }
		 return result.get();
	}
	
	public ProductImage insertImage(ProductImage newImage) {
		return imageRepository.save(newImage);
	}

	public boolean check() {
		return imageRepository.count() > 0 ? true : false;
	}

	public Integer getMaxSequenceNumber(int productID) {
		return imageRepository.getMaxImageSequenceNumber(productID);
	}
	
	public ProductImage findImageBySequence(List<ProductImage> productImages, Integer sequence) {
		for (ProductImage productImage : productImages) {
			if(productImage.getImageSequence() == sequence) {
				return productImage;
			}
		}
		return null;
	}
	
	public void deleteAndUpdateSequence(int imageID, int productID, int sequence) {
		Optional<ProductImage> optionalImg = imageRepository.findById(imageID);
		if(optionalImg.isPresent()) {
			ProductImage deleteTarget = optionalImg.get();
			imageRepository.delete(deleteTarget);
			if(sequence < 4) {
				List<ProductImage> imagesNotUpdated = imageRepository.findUpdateImagesByProductID(productID, sequence);
				for (ProductImage productImage : imagesNotUpdated) {
					productImage.setImageSequence(productImage.getImageSequence() - 1);
					imageRepository.save(productImage);
				}
			}
		}
	}
	
	public ProductImage updateImage(int imageID, String filename, byte[] imageFile) {
		Optional<ProductImage> optionalImg = imageRepository.findById(imageID);
		if(optionalImg.isPresent()) {
			ProductImage target = optionalImg.get();
			target.setImageFileName(filename);
			target.setProductImage(imageFile);
			return imageRepository.save(target);
		}
		return null;
	}

}
