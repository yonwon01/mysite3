package com.bigdata2017.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bigdata2017.mysite.vo.UserVo;
import com.bigdata2017.security.Auth.Role;

/**
 * 얘는 클라이언트에서 들어오는 모든 요청(/**)의 인터셉터를 하기 위한 목적으로 만든 것이라, DefaultServletHandler도 같이 넘어올 수 있다. 
 * 따라서 1번에서 handlerMethod가 아닌지 확인을 해야한다.(아닐경우 DefaultServletHandler임)
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//1. handler 종류 확인
		if( handler instanceof HandlerMethod == false ) {
			return true;	//default는 그냥 패스
		}
		
		//2. casting
		HandlerMethod handlerMethod = (HandlerMethod)handler;

		/* 메서드 -> 클래스 순으로 어노테이션을 검색하면 쉽다. */
		//3. handlerMethod 객체에서 Method의 @Auth 받아오기
		Auth auth = handlerMethod.getMethodAnnotation( Auth.class );
		
		if(auth == null) {
			//4. Method에 @Auth가 없다면, Class(Type)에 붙어있는 @Auth를 받아온다.
			auth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);
		}
		
		//5. @Auth가 안붙어 있는 경우
		if( auth == null ) {
			return true;
		}
		
		//6. @Auth가 붙어 있기 인증여부 체크(인증 안하고 바로 주소를 친 경우)
		HttpSession session = request.getSession();
		
		if( session == null ) {	// 인증이 안되어 있음
			response.sendRedirect( request.getContextPath() + "/user/login");
			return false;
		}
		
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		
		if(authUser == null) {	// 인증이 안되어 있음
			response.sendRedirect( request.getContextPath() + "/user/login");
			return false;
		}
		
		//7. @Auth에서 가져온 것이 "user"인지, "admin"인지를 확인한 후 처리(일반 사용자로 접속 후, url에 /admin을 입력해서 접근하는 경우를 막음)
		Role role = auth.role();
		
		/*
		 * 1. 사용자가 사용자 페이지 : true
		 * 2. 사용자가 관리자 페이지 : false
		 * 3. 관리자가 사용자 페이지 : true
		 * 4. 관리자가 관리자 페이지 : true
		 */
		if(role.equals(Auth.Role.ADMIN)){
			if(authUser.getRole().equals(Auth.Role.ADMIN.toString())) {
				return true;
			} else {
				/* 1. globalException으로 보내는 방법 */
				throw new Exception();
				/* 2. 직접 error url로 보내기 */
//				request.getRequestDispatcher( "/WEB-INF/views/error/admin_exception.jsp").forward(request, response);
//				return false;
			}
		}
		
		return true;
	}
}
