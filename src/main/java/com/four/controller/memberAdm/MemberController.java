package com.four.controller.memberAdm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.four.model.memberAdm.AdminBean;
import com.four.model.memberAdm.MemberBean;
import com.four.service.memberAdm.AdminService;
import com.four.service.memberAdm.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private MemberService memberService;
	
	// 首頁
	@GetMapping("/")
	public String homePageAction() {
		return "products/mall/HomePage";
	}
	
	// 登入註冊頁面
	@GetMapping("/loginregister.controller")
	public String loginregisterAction() {
		return "memberAdm/login";
	}
	
	// 登入
	@PostMapping("/loginPost.controller")
	public String loginAction(@RequestParam("admEmail") String admEmail, @RequestParam("admPassword") String admPassword,HttpSession httpSession, Model model) {
		AdminBean admin = null;
		MemberBean user = null;
		if ((admin = adminService.checkLogin(admEmail, admPassword)) != null) {
			httpSession.setAttribute("admin", admin);
			return "redirect:/admin/index";
        } else if((user = memberService.checkLogin(admEmail, admPassword)) != null) {
        	if(user.getStatus() == 2) {
        		return "redirect:/loginregister.controller?block=true";
        	}
            httpSession.setAttribute("user", user);
            model.addAttribute("member", user);
            return "redirect:/home";
        } else {
        	return "redirect:/loginregister.controller?error=true";
        }
	}
	// 前台會員中心
	@GetMapping("/profile.controller")
	public String profileAction(HttpSession httpSession, Model model) {
		MemberBean member = (MemberBean) httpSession.getAttribute("user");
		member = memberService.findById(member.getMemNo());
		model.addAttribute("member", member);
		return "memberAdm/profile";
	}
	
	// 註冊
	@PostMapping("/registerPost.controller")
	public String registerAction(@RequestParam("memName") String memName, 
			@RequestParam("memEmail") String memEmail, 
			@RequestParam("memPassword") String memPassword, 
			@RequestParam("gender") String genderStr, 
			@RequestParam("birth") String birthStr, 
			@RequestParam("phone") String phone, 
			Model model) {
		// 轉換 birth 日期字串為 LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthStr, formatter);
        // 設置 regDate 為今天的日期 轉字串
        LocalDate regDate = LocalDate.now();
        DateTimeFormatter regDateF = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 可以選擇合適的格式
        String regDateStr = regDate.format(regDateF);
        // 計算 age
        int age = Period.between(birth, LocalDate.now()).getYears();
 
 		MemberBean member = new MemberBean();
 		member.setMemName(memName);
        member.setMemEmail(memEmail);
        member.setMemPassword(memPassword);
        member.setGender(Integer.parseInt(genderStr));
        member.setBirth(birthStr);
        member.setAge(age);
        member.setPhone(phone);
        member.setRegDate(regDateStr);
        member.setStatus(0);
        member.setNickName("");
 		memberService.saveMember(member);
// 		return "memberAdm/login";
 		return "redirect:/loginregister.controller?success=true&memNo=" + member.getMemNo();
	}
	
	// 登出
	@GetMapping("/logout.controller")
	public String logoutAction(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/home";
	}
	
	// 查詢
	@GetMapping("/admin/getallmembers.controller")
	public String getAllMembersAction(Model m) {
		List<MemberBean> members = memberService.findAll();
		m.addAttribute("members", members);
		return "memberAdm/GetAllMems";
	}
	@GetMapping("/admin/getonemember.controller")
	public String getonememberAction(@RequestParam("memNo") Integer memNo, Model model) {
		MemberBean member = memberService.findById(memNo);
		
		model.addAttribute("member", member);
		return "memberAdm/GetMem";
	}
	
	// 新增
	@GetMapping("/admin/insertmembers.controller")
	public String insertMembersAction() {
		return "memberAdm/insert";
	}
	@PostMapping("/admin/insertPost.controller")
	public String insertPostAction(@RequestParam("memName") String memName, 
			@RequestParam("memEmail") String memEmail, 
			@RequestParam("memPassword") String memPassword, 
			@RequestParam("gender") String genderStr, 
			@RequestParam("birth") String birthStr, 
			@RequestParam("phone") String phone, 
			@RequestParam("nickName") String nickName, 
			Model m) {
		// 轉換 birth 日期字串為 LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthStr, formatter);
        // 設置 regDate 為今天的日期 轉字串
        LocalDate regDate = LocalDate.now();
        DateTimeFormatter regDateF = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 可以選擇合適的格式
        String regDateStr = regDate.format(regDateF);
        // 計算 age
        int age = Period.between(birth, LocalDate.now()).getYears();
 
 		MemberBean member = new MemberBean();
 		member.setMemName(memName);
        member.setMemEmail(memEmail);
        member.setMemPassword(memPassword);
        member.setGender(Integer.parseInt(genderStr));
        member.setBirth(birthStr);
        member.setAge(age);
        member.setPhone(phone);
        member.setRegDate(regDateStr);
        member.setNickName(nickName);
 		memberService.saveMember(member);
		
		return "redirect:/admin/getallmembers.controller";
	}
	
	// 顯示圖片
	@GetMapping("/showphoto")
	public ResponseEntity<byte[]> downloadPhotos(@RequestParam Integer memNo) {
		
		MemberBean member = memberService.findById(memNo);
		
		byte[] memPic = member.getMemPic();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		                                  // body,   headers, http status code
		return new ResponseEntity<byte[]>(memPic, headers, HttpStatus.OK);
	}
	
	// 更新
	@GetMapping("/admin/getupdatemember.controller")
	public String getupdatememberAction(@RequestParam("memNo") Integer memNo, Model model) {
		MemberBean member = memberService.findById(memNo);
		
		model.addAttribute("member", member);
		return "memberAdm/UpdateMem";
	}
	// 更新表單
	@PostMapping("/admin/updatemember.controller")
	public String updatememberAction(@ModelAttribute MemberBean updateBean, @RequestParam MultipartFile file, Model model) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(updateBean.getBirth(), formatter);
        int age = Period.between(birth, LocalDate.now()).getYears();
        updateBean.setAge(age);
		
		if (file != null && !file.isEmpty()) {
//			String fileName = file.getOriginalFilename();
			byte[] b = file.getBytes();
			updateBean.setMemPic(b);
		} else {		
			MemberBean oldMember = memberService.findByMemEmail(updateBean.getMemEmail());
			if (oldMember != null && oldMember.getMemPic() != null) {
				updateBean.setMemPic(oldMember.getMemPic());}
		}

        MemberBean member = memberService.update(updateBean);
        member = memberService.findById(updateBean.getMemNo());
        model.addAttribute("member", member);
        
        return "memberAdm/GetMem";
	}
	// 前台會員更新
	@PostMapping("/member/updatemember.controller")
	public String updatememberProfile(@ModelAttribute MemberBean updateBean, @RequestParam MultipartFile file, Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(updateBean.getBirth(), formatter);
        int age = Period.between(birth, LocalDate.now()).getYears();
        updateBean.setAge(age);
		
		if (file != null && !file.isEmpty()) {
			byte[] b = file.getBytes();
			updateBean.setMemPic(b);
		} else {		
			MemberBean oldMember = memberService.findByMemEmail(updateBean.getMemEmail());
			if (oldMember != null && oldMember.getMemPic() != null) {
				updateBean.setMemPic(oldMember.getMemPic());}
		}
		
		MemberBean member = memberService.update(updateBean);
        member = memberService.findById(updateBean.getMemNo());
		httpSession.setAttribute("user", member); // 更新 session 中的用戶資料
        
        return "redirect:/profile.controller"; 
	}
	
	// 封鎖
	@GetMapping("/admin/blockmember.controller")
	public String blockmemberAction(@RequestParam("memNo") Integer memNo) {
		memberService.blockById(memNo);
		return "redirect:/admin/getallmembers.controller";
	}
	
	// 聊天室
	@GetMapping("/userChat")
    public String userChat(HttpSession httpSession, Model model) {
		MemberBean member = (MemberBean) httpSession.getAttribute("user");
		member = memberService.findById(member.getMemNo());
		model.addAttribute("member", member);
        return "memberAdm/user_chat";
    }

    @GetMapping("/agentChat")
    public String agentChat(@RequestParam("memNo") Integer memNo, Model model) {
    	MemberBean member = memberService.findById(memNo);
		model.addAttribute("member", member);
        return "memberAdm/agent_chat";
    }

    @GetMapping("/adminChatList")
    public String adminChatList() {
        return "memberAdm/admin_chat_list";
    }
	
}

