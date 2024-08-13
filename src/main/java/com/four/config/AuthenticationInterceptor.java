package com.four.config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationInterceptor implements HandlerInterceptor {
	
	// 攔截器判斷
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURI();
		
		// 如果是公開頁面，可以直接進入
		if(requestUri.equals("/public/**")) {
			return true;
		}
		// 或是取得session可以進入
		HttpSession session = request.getSession(false);
		if(session != null && session.getAttribute("admin") != null) {
			return true;
		}
		
		// 沒有登錄，重新定向到登錄頁面
		response.sendRedirect("/workout/loginregister.controller");
		return false;
	}

}
