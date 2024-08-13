package com.four.model.memberAdm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminBean, Integer> {
	
	AdminBean findByAdmEmail(String admEmail);

}
