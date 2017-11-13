package com.bigdata2017.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bigdata2017.mysite.service.UserService;
import com.bigdata2017.mysite.vo.UserVo;

/**
 * 인증과 로그아웃을 할때 session객체를 controller단에서 사용하지 않게 하기 위해
 * 얘가 모든 인증 처리를 다 해주고 controller로 보내지 않게 하는 역할 -> main으로 다시 응답만 해 준다.
 */
public class AuthLoginInterceptor extends HandlerInterceptorAdapter {

	/* interceptor도 web application context에 만들어지는 놈이니까 아래처럼 콘테이너를 직접 빼 와야 할 필요가 없다.
	 * Spring container에 만들어지기 때문에 DI를 활용해 아래처럼 @Autowired로 연결될 수 있다. */
	@Autowired
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		/* ------------------------------------- @Autowired로 대체 ---------------------------------------- */
		
		/* spring에서는 root app context와 web app context가 존재하는데, 그 중 web app context를 가져오는 방법(context안의 자원을 사용하기 위함) */
		/* interceptor는 spring web container 안에 존재하는 놈이 아니니까 따로 container를 가져와야만 사용할 수 있다. 이러면 아무데서나 콘테이너를 받아 사용할 수 있다. */
//		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		
		/* 가져온 context에서 UserService 자원을 가져와 아래처럼 사용할 수 있다. */
//		UserService userService = ac.getBean( UserService.class );
		
		/* ------------------------------------- @Autowired로 대체 ---------------------------------------- */
		
		
		UserVo userVo = userService.getUser(email, password);
		
		if( userVo == null ) {
			response.sendRedirect( request.getContextPath() + "/user/login" );
			return false;
		}
		
		/* 여기까지 왔으면 인증이 된 것이니 session 처리 */
		/* return false니까 뒷 부분(controller)쪽으로 가지 않고 여기서 처리함 */
		/* request.getSession( true ); <-true면 session이 없으면 만들어서 내 놓아라 */
		HttpSession session = request.getSession( true );
		session.setAttribute("authUser", userVo);
		response.sendRedirect(request.getContextPath());
		
		return false;
	}
}
