package com.bigdata2017.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bigdata2017.mysite.vo.UserVo;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		/* @AuthUser가 안붙어있으면 false, 다른 어노테이션을 정의하면 그것도 이런식으로 처리 */
		if(supportsParameter(parameter) == false) {
			/* 내가 해석할 수 없는 놈이다. => 다른 resolver에게 넘긴다. */
			return WebArgumentResolver.UNRESOLVED;
		}
		
		/* @AuthUser가 붙어있고 파라미터 타입이 UserVo */
		HttpServletRequest request = webRequest.getNativeRequest( HttpServletRequest.class );
		
		HttpSession session = request.getSession();
		if( session == null ) {
			return null;
		}
		
		return session.getAttribute( "authUser" );
	}

	/*  */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		AuthUser authUser = parameter.getParameterAnnotation( AuthUser.class );
		
		/* @AuthUser가 안 붙어 있음  */
		if( authUser == null ) {
			return false;
		}
		
		/* 파라미터 타입이 UserVo가 아님 */
		if( parameter.getParameterType().equals(UserVo.class) == false ) {
			return false;
		}
		
		return true;
	}

}
