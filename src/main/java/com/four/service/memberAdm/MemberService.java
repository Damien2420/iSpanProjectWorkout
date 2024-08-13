package com.four.service.memberAdm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.four.model.memberAdm.MemberAgeDTO;
import com.four.model.memberAdm.MemberBean;
import com.four.model.memberAdm.MemberRegistrationDTO;
import com.four.model.memberAdm.MemberRepository;


@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public MemberBean findById(Integer memNo) {
		
		Optional<MemberBean> optional = memRepo.findById(memNo);
		
		if(optional.isEmpty()) {
			return null;
		}
		return optional.get();
	
	}
	public List<MemberBean> findAll(){
		return memRepo.findAll();
	}

	public MemberBean saveMember(MemberBean insertBean) {
		String encodedPwd = passwordEncoder.encode(insertBean.getMemPassword()); // 加密
		insertBean.setMemPassword(encodedPwd); // 存加密過後的
		return memRepo.save(insertBean);
	}
	
	// google登入
	public MemberBean addMemberByGoogle(String memEmail, String memName, String regDateStr) {
		MemberBean member = new MemberBean();
		member.setMemEmail(memEmail);
		member.setMemName(memName);
		member.setIsGoogleLogin(Boolean.TRUE);
		member.setRegDate(regDateStr);
		member.setStatus(1);
		return memRepo.save(member);
	}

	public MemberBean update(MemberBean updateBean) {
		return memRepo.save(updateBean);
	}

//	public boolean deleteById(Integer memNo) {
//		return memRepo.deleteById(memNo);
//	}
	
	@Transactional
	@Modifying
	public void blockById(Integer memNo) {
		
		Optional<MemberBean> optional = memRepo.findById(memNo);
		
		if(optional != null) {
			MemberBean resultBean = optional.get();
			resultBean.setStatus(2);
			memRepo.save(resultBean);
		}
		
	}
	
	// 檢查有沒有被註冊過
	public boolean checkMemEmailExist(String memEmail) {
		
		MemberBean dbMember = memRepo.findByMemEmail(memEmail);
		
		if(dbMember == null) {
			return false;
		}
		return true;
	}
	
	// 確認帳密是否正確
	public MemberBean checkLogin(String memEmail, String memPassword) {
		
		MemberBean dbMember = memRepo.findByMemEmail(memEmail);
		
		if(dbMember == null) {
			return null;
		} else {
			String dbPassword = dbMember.getMemPassword();
			boolean result = passwordEncoder.matches(memPassword, dbPassword); // .matches()可以比對加密過後密碼
			
			if(result) {
				return dbMember;
			}
			return null;
		}
	}
	
	public MemberBean findByMemEmail(String memEmail) {
		return memRepo.findByMemEmail(memEmail);
	}
	
	// 重設密碼
	@Transactional
	@Modifying
	public void resetPassword(Integer memNo, String memPassword) {
		Optional<MemberBean> optional = memRepo.findById(memNo);
		MemberBean resultBean = optional.get();
		
		String encodedPwd = passwordEncoder.encode(memPassword); // 加密
		resultBean.setMemPassword(encodedPwd); // 存加密過後的
		memRepo.save(resultBean);
	}
	
	// 取得每月註冊人數
	// MemberRegistrationData 是你定義的一個 DTO (Data Transfer Object)，需要根據自己的數據結構來實現這個方法
	public List<MemberRegistrationDTO> getMemberRegistrationData() {
        List<Object[]> results = memRepo.findMemberRegistrationData();
        List<MemberRegistrationDTO> dataList = new ArrayList<>();

        // 手動將查詢結果轉換為 MemberRegistrationData 對象
        for (Object[] result : results) {
            Integer month = ((Number) result[0]).intValue();
            Long count = ((Number) result[1]).longValue();
            dataList.add(new MemberRegistrationDTO(month, count));
        }
        return dataList;
    }
	
	// 取得會員年齡人數  手動轉換(另一種方法)
	public List<MemberAgeDTO> getAgeData() {
        List<Object[]> results = memRepo.findMemberAgeGroups();
        return results.stream()
                .map(result -> new MemberAgeDTO((String) result[0], ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }
	
	public int getMemberCount() {
        return (int) memRepo.count();
    }

}

