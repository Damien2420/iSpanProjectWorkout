package com.four.service.memberAdm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.four.model.memberAdm.AdminBean;
import com.four.model.memberAdm.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	private AdminRepository admRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// 確認帳密是否正確
		public AdminBean checkLogin(String admEmail, String admPassword) {
			
			AdminBean dbAdmin = admRepo.findByAdmEmail(admEmail);
			
			if(dbAdmin == null) {
				return null;
			} else {
				String dbPassword = dbAdmin.getAdmPassword();
//				boolean result = passwordEncoder.matches(admPassword, dbPassword); // .matches()可以比對加密過後密碼
				
				if(dbPassword.equals(admPassword)) {
					return dbAdmin;
				}
				return null;
			}
		}
	
//	public AdminBean validate(String admEmail, String admPassword) {
//		return adminRepo.validate(admEmail, admPassword);
//	}

}

