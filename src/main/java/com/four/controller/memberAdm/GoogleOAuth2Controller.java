package com.four.controller.memberAdm;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.four.config.GoogleOAuth2Config;
import com.four.model.memberAdm.ContactForm;
import com.four.model.memberAdm.MemberBean;
import com.four.service.memberAdm.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class GoogleOAuth2Controller {
	
	@Autowired
	private GoogleOAuth2Config googleOAuth2Config;
	
	@Autowired
	private MemberService memberService;
	
	private final String scope = "https://www.googleapis.com/auth/userinfo.email";
	
	@GetMapping("/google-login")
	public String googleLogin(HttpServletResponse response) {
		String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" + 
				"client_id=" + googleOAuth2Config.getClientId() + 
				"&response_type=code" + 
				"&scope=openid%20email%20profile" + 
				"&redirect_uri=" + googleOAuth2Config.getRedirectUri() + 
				"&state=state";
		
		System.out.println(authUrl);
		return "redirect:" + authUrl;
	}
	
	@GetMapping("/google-callback")
	public String googleCallback(@RequestParam(required = false) String code, Model model, HttpSession httpSession) {
		if(code == null) {
			String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" + 
					"client_id=" + googleOAuth2Config.getClientId() + 
					"&redirect_uri=" + googleOAuth2Config.getRedirectUri() + 
					"&scope=" + scope;
			return "redirect:" + authUrl;
		} else {
			RestClient restClient = RestClient.create();
			try { // 拿 code 換 token
				// 準備 requestbody
				String requestBody = UriComponentsBuilder.newInstance()
					.queryParam("code", code)
					.queryParam("client_id", googleOAuth2Config.getClientId())
					.queryParam("client_secret", googleOAuth2Config.getClientSecret())
					.queryParam("redirect_uri", googleOAuth2Config.getRedirectUri())
					.queryParam("grant_type", "authorization_code")
					.build()
					.getQuery();
				
				String credentials = restClient.post()
				.uri("https://oauth2.googleapis.com/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(requestBody)
				.retrieve()
				.body(String.class);
				
				System.out.println("credentials" + credentials);
				
				// json 字串轉換為 JsonNode (Java物件)
				JsonNode jsonNode = new ObjectMapper().readTree(credentials); // 解析
				String accessToken = jsonNode.get("access_token").asText();
				String idToken = jsonNode.get("id_token").asText();
								
				// 拿到 token 後再去 google 請求一次，找user
				String payloadResponse = restClient.get()
					.uri("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken)
					.header("Authorization", "Bearer " + idToken)
					.retrieve()
					.body(String.class);
				
				System.out.println("payloadResponse" + payloadResponse);
				
				JsonNode userinfo = new ObjectMapper().readTree(payloadResponse);
				String loginUserEmail =userinfo.get("email").asText();
				String loginUserName =userinfo.get("name").asText();
				
				boolean result = memberService.checkMemEmailExist(loginUserEmail);
				
				if(result) {
					MemberBean member = memberService.findByMemEmail(loginUserEmail);
					httpSession.setAttribute("user", member);
					return "front/index";
				} else {
					// 設置 regDate 為今天的日期 轉字串
			        LocalDate regDate = LocalDate.now();
			        DateTimeFormatter regDateF = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 可以選擇合適的格式
			        String regDateStr = regDate.format(regDateF);
					MemberBean member = memberService.addMemberByGoogle(loginUserEmail, loginUserName, regDateStr);
					httpSession.setAttribute("user", member);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "memberAdm/login";
			}
			return "redirect:/profile.controller?insertInfo=true";
		}
	}
	
	@PostMapping("/updateMemberInfo")
	@ResponseBody
	public ResponseEntity<String> updateMemberInfo(@RequestBody Map<String, String> updateInfo, HttpSession httpSession) {
	    MemberBean member = (MemberBean) httpSession.getAttribute("user");
	    if (member != null) {
	    	String genderStr = updateInfo.get("gender");
	    	Integer gender = Integer.parseInt(genderStr);
	        String birthStr = updateInfo.get("birthdate");
	        // 轉換 birth 日期字串為 LocalDate
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate birth = LocalDate.parse(birthStr, formatter);
	        int age = Period.between(birth, LocalDate.now()).getYears();
	        member.setGender(gender);
	        member.setBirth(birthStr);
	        member.setAge(age);

	        MemberBean updatemember = memberService.update(member);
			httpSession.setAttribute("user", updatemember); // 更新 session 中的用戶資料
	        return ResponseEntity.ok("Member information updated successfully.");
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
	    }
	}

	
	@Autowired
    private JavaMailSender mailSender;
	
	// 寄驗證信畫面
	@GetMapping("/test-email")
    public String sendVerificationEmail(@RequestParam("memNo") Integer memNo, RedirectAttributes redirectAttributes, Model model) {
        MemberBean member = memberService.findById(memNo); // 根据memNo查找Member

        if (member != null) {
        	sendVerificationEmail(member.getMemNo(), member.getMemEmail());
            model.addAttribute("member", member);
        } 
//        return "redirect:/confirmation-email.controller"; // 重定向到某個頁面
        return "memberAdm/confirmation-email";
    }
	@GetMapping("/resend-email")
	public ResponseEntity<String> sendTestEmail(@RequestParam("memNo") Integer memNo) {
		MemberBean member = memberService.findById(memNo);
		sendVerificationEmail(member.getMemNo(), member.getMemEmail());
	    return ResponseEntity.ok("Email sent");
	}
	// 寄驗證信 method
	private void sendVerificationEmail(Integer memNo, String toEmail) {
		String verificationLink = "http://localhost:8081/workout/verify-email?memNo=" + memNo;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification");
        message.setText("Please verify your email by clicking the following link: " + verificationLink);
        mailSender.send(message);
	}
	
	// 點擊驗證信連結回到會員中心
	@GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("memNo") Integer memNo, RedirectAttributes redirectAttributes, HttpSession httpSession, Model model) {
        MemberBean member = memberService.findById(memNo);

        if (member != null) {
            member.setStatus(1);
            memberService.update(member); // 保存狀態變更
        } 
//        httpSession.setAttribute("user", member);
//        model.addAttribute("member", member);
        httpSession.invalidate();
//        return "memberAdm/profile";
        return "redirect:/loginregister.controller?verify=true";
    }
	
	
	// 忘記密碼
	@PostMapping("/forgetPassword")
	@ResponseBody
	public String forgetPasswordAction(@RequestParam("email") String memEmail) {
		MemberBean member = memberService.findByMemEmail(memEmail);
		sendForgetPasswordEmail(member.getMemNo(), member.getMemEmail());
//	    return "memberAdm/login";
		return "重置密碼的連結已經發送到您的電子郵件地址！";
	}
	// 寄重設密碼信 method
	private void sendForgetPasswordEmail(Integer memNo, String toEmail) {
		String verificationLink = "http://localhost:8081/workout/resetPassword-email?memNo=" + memNo;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("重置密碼請求");
        message.setText("請點擊以下連結來重置您的密碼: " + verificationLink);
        mailSender.send(message);
	}
	// 點擊忘記密碼連結到重設密碼頁面
	@GetMapping("/resetPassword-email")
    public String resetPasswordEmail(@RequestParam("memNo") Integer memNo, RedirectAttributes redirectAttributes, HttpSession httpSession, Model model) {
        MemberBean member = memberService.findById(memNo);

        model.addAttribute("member", member);
        return "memberAdm/resetPassword";
    }
	@PostMapping("/resetPassword")
	public String resetPasswordAction(@RequestParam("memNo") Integer memNo,
            @RequestParam("memPassword") String memPassword,
            @RequestParam("confirmPassword") String confirmPassword) {
		memberService.resetPassword(memNo, memPassword);
//		return "memberAdm/login";
		return "redirect:/loginregister.controller?resetPwdSuccess=true";
	}
	
	// 前台Contact Us 寄email
	@PostMapping("/sendContactEmail")
	public ResponseEntity<String> sendContactEmail(@RequestBody ContactForm contactForm) {
	    try {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo("eeit83group4@gmail.com");
	        message.setSubject("Contact Form Submission from " + contactForm.getName());
	        message.setText("Name: " + contactForm.getName() + "\nEmail: " + contactForm.getEmail() + "\nMessage: " + contactForm.getComments());
	        mailSender.send(message);
	        return ResponseEntity.ok("Email sent successfully.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email.");
	    }
	}


}
