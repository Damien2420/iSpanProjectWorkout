package com.four.controller.product;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.four.model.memberAdm.MemberBean;
import com.four.model.order.Order;
import com.four.model.products.Product;
import com.four.model.products.ProductCategory;
import com.four.model.products.ProductImage;
import com.four.model.products.ProductReview;
import com.four.service.memberAdm.MemberService;
import com.four.service.order.OrderService;
import com.four.service.products.ProductCategoryService;
import com.four.service.products.ProductImageService;
import com.four.service.products.ProductReviewService;
import com.four.service.products.ProductService;
import com.four.utils.CartDTO;
import com.four.utils.HeaderSetter;

import jakarta.servlet.http.HttpSession;

@Controller
public class MallController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private ProductCategoryService categoryService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ProductReviewService reviewService;
	@Autowired
	private PagedResourcesAssembler<Product> pagedResourcesAssembler;
	
	//new mall
	@GetMapping("/mall")
	public String goMallPage(Model model) {
		Page<Product> allProducts = productService.mallsearchLaunchedProductsByPage(1);
		List<ProductCategory> allCategories = categoryService.searchAllCategories();
		List<Product> recommendProducts = productService.randomFourProducts();
		model.addAttribute("recommend", recommendProducts);
		model.addAttribute("products", allProducts);
		model.addAttribute("categories", allCategories);
		return "products/mall/Mall";
	}
	
	//取得特定類別的所有商品
	@ResponseBody
	@GetMapping("/mall/category/{id}")
	public PagedModel<EntityModel<Product>> getCategoryProducts(@PathVariable(name = "id") Integer categoryID, 
																@RequestParam("page") Integer pageID) {
		Page<Product> targetPage = null;
		if(categoryID == 0) {
			targetPage = productService.mallsearchLaunchedProductsByPage(pageID);
		}else {
			targetPage = productService.findCategoryProductsByPage(categoryID, pageID);			
		}
		return pagedResourcesAssembler.toModel(targetPage);
	}
	
	//搜尋商品
	@ResponseBody
	@GetMapping("/mall/search")
	public PagedModel<EntityModel<Product>> searchProducts(@RequestParam("key") String keyword,
														   @RequestParam(name = "page", defaultValue = "1") int pageNumber) {
		Page<Product> targetPage = productService.fuzzySearchByProductname(keyword, pageNumber);
		return pagedResourcesAssembler.toModel(targetPage);
	}
	
	//商品詳細頁面 & 隨機4個推薦商品
	@GetMapping("/mall/productDetails")
	public String getProductDetail(@RequestParam("id")int selectedProductID, Model model) {

		Product productSearchResult = productService.singleSearchProduct(selectedProductID);
		List<ProductImage> productImages = productSearchResult.getProductImage();
		List<Product> recommendProducts = productService.randomFourProducts();
		List<ProductReview> allRatings = reviewService.findAllReviewByProduct(selectedProductID);
		Map<String, Integer> ratingGroup = null;
		Double averageRatings = null;
		if(allRatings.size() != 0) {
			ratingGroup = reviewService.allReviewPercentagesByProductID(selectedProductID);
			averageRatings = Math.round(reviewService.getAllRatingCountsAndAverageRatings(selectedProductID) * 10.0) / 10.0;		
		}
		
		model.addAttribute("selectedProduct", productSearchResult);
		model.addAttribute("productImage", productImages);
		model.addAttribute("recommend", recommendProducts);
		model.addAttribute("ratingGroup", ratingGroup);
		model.addAttribute("allReviews", allRatings);
		model.addAttribute("avgRating", averageRatings);
		
		if(!model.containsAttribute("inTheCart")) {
			model.addAttribute("inTheCart", false);			
		}
		return "products/mall/ProductDetail";
	}
	
	@GetMapping("/mall/cart")
	public String cartPage() {
		return "products/mall/ShoppingCart";
	}
	
	@ResponseBody
	@DeleteMapping("/mall/emptyCart")
	public ResponseEntity<Void> emptyCart(HttpSession session) {
		session.removeAttribute("cart");
		return ResponseEntity.ok().build();
	}
	
	@ResponseBody
	@SuppressWarnings("unchecked")
	@PostMapping("/mall/addToCart")
	public ResponseEntity<Integer> addIntoCart(@RequestBody Map<String, String> product, HttpSession session) {
		List<CartDTO> cart = (List<CartDTO>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute("cart", cart);
		}
		
		Integer productID = Integer.parseInt(product.get("productID"));
		Integer quantity = Integer.parseInt(product.get("quantity"));
		
		for (CartDTO item : cart) {
			Integer id = item.getProduct().getProductID();
			if(productID == id) {
				productService.updateCart(cart, productID, quantity, "change");
				session.setAttribute("cart", cart);
				return ResponseEntity.ok().build();
			}
		}
		
		Product resultProduct = productService.singleSearchProduct(productID);
		ProductImage image = resultProduct.getProductImage().get(0);
		MemberBean user = (MemberBean) session.getAttribute("user");
		Integer memberNo = user.getMemNo();
		CartDTO item = new CartDTO(memberNo, resultProduct, image, quantity);
		cart.add(item);
		
		session.setAttribute("cart", cart);
		return ResponseEntity.ok(cart.size());
	}
	
	//更新購物車商品下單數量ajax
	@SuppressWarnings("unchecked")
	@ResponseBody
	@PostMapping("/mall/updateCart")
	public ResponseEntity<Void> updateCart(@RequestBody Map<String, String> cartItem, HttpSession session) {
		List<CartDTO> cart = (List<CartDTO>) session.getAttribute("cart");
		Integer target = Integer.parseInt(cartItem.get("productID"));
		Integer quantity = Integer.parseInt(cartItem.get("quantity"));
		String action = cartItem.get("action");
		
		cart = productService.updateCart(cart, target, quantity, action);
		if(cart.size() == 0) {
			session.removeAttribute("cart");
		} else {
			session.setAttribute("cart", cart);			
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/mall/purchaseSuccess")
	public String goSuccessPage(@RequestParam String id, Model model) {
		Order order = orderService.findOrderByOrderID(id);
		MemberBean member = memberService.findById(order.getMemberID());
        model.addAttribute("order", order);
        model.addAttribute("member", member);
		return "products/mall/PurchaseSuccess";
	}
	
	//使用商品編號取得第一張圖片
	@GetMapping("/image/product")
	public ResponseEntity<byte[]> getProductFirstImage(@RequestParam("id") Integer productID) {
		ProductImage imageFound = productImageService.findFirstImageByProductID(productID);
		String fileName = imageFound.getImageFileName();
		HttpHeaders header = HeaderSetter.setHeader(fileName);
		return new ResponseEntity<byte[]>(imageFound.getProductImage(), header, HttpStatus.OK);
	}
}
