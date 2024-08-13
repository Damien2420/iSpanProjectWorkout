package com.four.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.four.utils.ecpay.payment.integration.AllInOne;
import com.four.utils.ecpay.payment.integration.domain.AioCheckOutALL;

@Configuration
public class AppConfig {
	
	@Bean
	PasswordEncoder passwordEncoler() { // 前面public 可以不用寫
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AllInOne allInOne() {
		return new AllInOne("");
	}
	
	@Bean
	AioCheckOutALL aioCheckOutALL() {
		return new AioCheckOutALL();
	}
}
