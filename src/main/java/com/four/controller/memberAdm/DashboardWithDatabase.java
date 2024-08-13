package com.four.controller.memberAdm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.four.model.memberAdm.MemberAgeDTO;
import com.four.model.memberAdm.MemberBean;
import com.four.model.memberAdm.MemberRegistrationDTO;
import com.four.service.memberAdm.MemberService;

@RestController
public class DashboardWithDatabase {
	
	@Autowired
	private MemberService memberService;
	
	// 性別環形圖
	@GetMapping("/api/member-gender")
    public Map<String, Object> getChartData() {
        List<MemberBean> members = memberService.findAll();
        int maleCount = 0;
        int femaleCount = 0;
        for (MemberBean member : members) {
            if (member.getGender() == 0) {
                maleCount++;
            } else if (member.getGender() == 1) {
                femaleCount++;
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("labels", new String[]{"女", "男"});
        data.put("data", new int[]{femaleCount, maleCount});

        return data;
    }
	
	// 註冊人數折線圖
	@GetMapping("/api/member-registration")
	public ResponseEntity<List<MemberRegistrationDTO>> getMemberRegistrationData() {
	    List<MemberRegistrationDTO> data = memberService.getMemberRegistrationData();
	    return ResponseEntity.ok(data);
	}
	
	// 會員年齡分佈柱狀圖
	@GetMapping("/api/member-age")
	public List<MemberAgeDTO> getAgeData() {
        return memberService.getAgeData();
    }
	
	// 會員總人數
	@GetMapping("api/member-count")
    public ResponseEntity<Integer> getMemberCount() {
        int memberCount = memberService.getMemberCount();
        return ResponseEntity.ok(memberCount);
    }
	
	
	// 檢查email有沒有被註冊過
	@PostMapping("/checkEmail")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean exists = memberService.checkMemEmailExist(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

}
