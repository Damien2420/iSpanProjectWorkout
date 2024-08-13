package com.four.controller.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.four.model.products.Product;
import com.four.model.products.ProductCategory;
import com.four.model.products.ProductImage;
import com.four.service.products.ProductCategoryService;
import com.four.service.products.ProductImageService;
import com.four.service.products.ProductService;
import com.four.utils.HeaderSetter;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductManagementController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService categoryService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private ObjectMapper objectMapper;
    @Autowired
    private PagedResourcesAssembler<Product> pagedResourcesAssembler;
	
	//商品列表
	@GetMapping("/admin/products/allList")
	public String showFirstPageAllProduct(Model model) {
		Page<Product> allProducts = productService.searchAllProductByPage(1);
		model.addAttribute("allProducts", allProducts);
		if(!model.containsAttribute("status")) {
			model.addAttribute("status", false);
		}
		return "products/management/AllProducts";
	}
	//商品列表ajax換頁
	@ResponseBody
	@GetMapping("/admin/products/allList/{pageNumber}")
	public PagedModel<EntityModel<Product>> showAllProductByPageUsingAjax(@PathVariable Integer pageNumber) {
		Page<Product> targetProductPage = productService.searchAllProductByPage(pageNumber);
		return pagedResourcesAssembler.toModel(targetProductPage);
	}
	
	//刪除商品
	@PostMapping("/admin/products/delete")
	@ResponseBody
	public Integer delete(@RequestBody Map<Integer, String> delete, RedirectAttributes redirect) {
		List<Integer> deleteIdList = new ArrayList<>();
		delete.forEach((key, value) -> {
            Integer id = Integer.parseInt(value);
            deleteIdList.add(id);
        });
		boolean deleteStatus = productService.deleteProducts(deleteIdList);
		return deleteStatus ? deleteIdList.size() : 0;
	}
	
	//更新前取得商品資訊
	@GetMapping("/admin/products/updateInfo")
	public String getProductDetailsBeforeUpdate(@RequestParam("productID") Integer productID, Model model) {
		List<ProductCategory> allCategories = categoryService.searchAllCategories();
		Product searchResult = productService.singleSearchProduct(productID);
		List<ProductImage> productImages = searchResult.getProductImage();
		
		model.addAttribute("allCategories", allCategories);
		model.addAttribute("selectedProduct", searchResult);
		model.addAttribute("productImage", productImages);
		if(!model.containsAttribute("status")) {
			model.addAttribute("status", false);
		}
		return "products/management/UpdateProductInfo";
	}
	
	//更新商品資訊
	@ResponseBody
	@PostMapping("/admin/products/update")
	public Product updateProduct(@RequestBody String productInfo) throws JsonMappingException, JsonProcessingException {
		Product newInfo = objectMapper.readValue(productInfo, Product.class);
		Product updatedProduct = productService.updateProduct(newInfo);
		return updatedProduct;
	}
	
	//更新圖片頁面
	@GetMapping("/admin/products/updateImage")
	public String updateProductImagesPage(@RequestParam("p") Integer productID, Model model) {
		Product targetProduct = productService.singleSearchProduct(productID);
		model.addAttribute("id", targetProduct.getProductID());
		model.addAttribute("images", targetProduct.getProductImage());
		return "products/management/UpdateImages";
	}
	
	//執行更新圖片
	@ResponseBody
	@PostMapping("/admin/products/updateImg")
	public ResponseEntity<Void> updateImage(@RequestParam("image") MultipartFile img,
											@RequestParam("metadata") String imageID) throws NumberFormatException, IOException {
		ProductImage updateImage = productImageService.updateImage(Integer.parseInt(imageID), img.getOriginalFilename(), img.getBytes());
		if(updateImage == null) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.ok().build();
	}
	
	//新增圖片
	@ResponseBody
	@PostMapping("/admin/products/saveImg")
	public ResponseEntity<Void> saveImages(@RequestParam("image") MultipartFile img,
										   @RequestParam("metadata") String sequence,
										   @RequestParam("productID") String productID) throws IOException {
		String filename = img.getOriginalFilename();
		byte[] imageFile = img.getBytes();
		Product product = productService.singleSearchProductWithoutImage(Integer.parseInt(productID));
		ProductImage newImage = new ProductImage(filename, Integer.parseInt(sequence), imageFile, product);
		productImageService.insertImage(newImage);
		return ResponseEntity.ok().build();
	}
	
	//刪除圖片
	@ResponseBody
	@DeleteMapping("/admin/products/deleteImages")
	public ResponseEntity<Void> deleteProductImages(@RequestBody Map<String, String> imageData) {
		int imageID = Integer.parseInt(imageData.get("imageID"));
		int sequence = Integer.parseInt(imageData.get("sequence"));
		int productID = Integer.parseInt(imageData.get("productID"));
		productImageService.deleteAndUpdateSequence(imageID, productID, sequence);
		return ResponseEntity.ok().build();
	}
	
	//進入查詢頁面
	@GetMapping("/admin/products/searchPage")
	public String goInSearchPage(Model model) {
		model.addAttribute("searchBegin", false);
		return "products/management/QueryResult";
	}
	
	//模糊查詢
	@GetMapping("/admin/products/fuzzySearch")
	public String fuzzySearch(@RequestParam("query") String query, Model model) {
		List<Product> searchResult = productService.fuzzySearchProductsByCategoryAndName(query);
		model.addAttribute("searchBegin", true);
		model.addAttribute("productsFound", searchResult);
		return "products/management/QueryResult";
	}
	
	//進入新增頁面前，查詢全部類別
	@GetMapping("/admin/products/searchAllCategory")
	public String getAllCategories(Model model) {
		List<ProductCategory> allCategories = categoryService.searchAllCategories();
		model.addAttribute("allCategories", allCategories);
		if(!model.containsAttribute("status")) {
			model.addAttribute("status", false);
		}
		Product emptyProduct = new Product();
		model.addAttribute("product", emptyProduct);
		return "products/management/InsertProduct";
	}
	
	//新增類別
	@ResponseBody
	@PostMapping("/admin/products/saveCategory")
	public ProductCategory addCategory(@RequestBody ProductCategory newCategory) {
		return categoryService.insertCategory(newCategory);
	}
	
	//商品詳細頁面
	@GetMapping("/admin/products/fullDetail")
	public String getProductDetails(@RequestParam("productID") Integer productID, Model model) {
		Product searchResult = productService.singleSearchProduct(productID);
		List<ProductImage> imageSearchResult = searchResult.getProductImage();
		model.addAttribute("selectedProduct", searchResult);
		model.addAttribute("imageData", imageSearchResult);
		return "products/management/SingleProductDetail";
	}
	
	@GetMapping("/image")
	public ResponseEntity<byte[]> getImage(@RequestParam("id") Integer imageId){
		ProductImage image = productImageService.findImageById(imageId);
		String fileName = image.getImageFileName();
		HttpHeaders header = HeaderSetter.setHeader(fileName);
		return new ResponseEntity<byte[]>(image.getProductImage(), header, HttpStatus.OK);
	}
	
	//新增頁面尚未有imageID取得圖片
	@SuppressWarnings("unchecked")
	@GetMapping("/imageBeforeSave")
	public ResponseEntity<byte[]> getImage(@RequestParam("t") Integer tempId, HttpSession session){
		Map<Integer, Product> saveList = (Map<Integer, Product>) session.getAttribute("saveList");
		ProductImage image = productImageService.findImageBySequence(saveList.get(tempId).getProductImage(), 1);
		String fileName = image.getImageFileName();
		HttpHeaders header = HeaderSetter.setHeader(fileName);
		return new ResponseEntity<byte[]>(image.getProductImage(), header, HttpStatus.OK);
	}
	
	//儲存新增產品資訊
	@SuppressWarnings("unchecked")
	@PostMapping("/admin/products/saveProduct")
	public ResponseEntity<Void> saveNewProductInfo(@RequestParam("id") Integer tempId,
	        									   @RequestParam("productCategory") Integer categoryID,
	        									   @RequestParam("productName") String productName,
	        									   @RequestParam("price") Integer price,
	        									   @RequestParam("stock") Integer stock,
	        									   @RequestParam("productFeatures") String productFeatures, HttpSession session) {
		Map<Integer, Product> saveList = (Map<Integer, Product>) session.getAttribute("saveList");
		if(saveList == null) {
			saveList = new HashMap<>();
		}
		ProductCategory category = categoryService.singleSearchCategory(categoryID);
		Product product = new Product(productName, price, null, stock, productFeatures, category);
		product.setProductImage(new ArrayList<>());
		saveList.put(tempId, product);
		
		session.setAttribute("saveList", saveList);
		return ResponseEntity.ok().build();
}
	
	//商品資訊儲存到待新增列表
	@SuppressWarnings("unchecked")
	@PostMapping("/admin/products/saveNewImg")
	public ResponseEntity<Void> uploadFile(@RequestParam("image") MultipartFile file,
	        							   @RequestParam("id") Integer tempId,
	        							   @RequestParam("sequence") Integer sequence, HttpSession session) throws IOException {
		Map<Integer, Product> saveList = (Map<Integer, Product>) session.getAttribute("saveList");

		String fileName = file.getOriginalFilename();
		byte[] imageFile = file.getBytes();
		ProductImage image = new ProductImage(fileName, sequence, imageFile, null);
		
		if(saveList.containsKey(tempId)) {
			Product newProduct = saveList.get(tempId);
			List<ProductImage> imageList = newProduct.getProductImage();
			imageList.add(image);
			newProduct.setProductImage(imageList);
			saveList.put(tempId, newProduct);
		}
		
		session.setAttribute("saveList", saveList);
		
		return ResponseEntity.ok().build();
	}
	
	//新增列表移除
	@SuppressWarnings("unchecked")
	@ResponseBody
	@PostMapping("/admin/products/saveListRemove")
	public ResponseEntity<Void> getSaveList(@RequestBody Integer tempID, HttpSession session){
		Map<Integer, Product> saveList = (Map<Integer, Product>) session.getAttribute("saveList");
		saveList.remove(tempID);
		session.setAttribute("saveList", saveList);
		return ResponseEntity.ok().build();
	}
	
	//新增列表輸入至資料庫
	@SuppressWarnings("unchecked")
	@ResponseBody
	@GetMapping("/admin/products/saveAll")
	public ResponseEntity<Void> saveAllProductOnList(HttpSession session) {
		Map<Integer, Product> saveList = (Map<Integer, Product>) session.getAttribute("saveList");
		saveList.forEach( (id, product) -> productService.insertProduct(product));
		session.removeAttribute("saveList");
		return ResponseEntity.ok().build();
	}

	//更新商品上下架
	@ResponseBody
	@PutMapping("/admin/products/updateLaunch/{id}")
	public ResponseEntity<Void> updatLaunchStatus(@PathVariable(name = "id") Integer productID, @RequestParam("status") boolean status) {
		Product product = productService.singleSearchProductWithoutImage(productID);
		product.setLaunchStatus(status);
		productService.updateProduct(product);
		return ResponseEntity.ok().build();
	}
}
