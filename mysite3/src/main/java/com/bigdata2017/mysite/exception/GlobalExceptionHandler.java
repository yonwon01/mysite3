package com.bigdata2017.mysite.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.taglibs.standard.tag.common.fmt.RequestEncodingSupport;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.bigdata2017.mysite.dto.JSONResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class GlobalExceptionHandler {

	/* 여기는 외부에 만들어주는 controller같은게 아니니까 외부 기술(httpServletRequest)이 들어와도 된다. */
	/* 이렇게 annotation을 붙이면 UserController로 던져지는 모든 exception은 다 이리 들어온다. */
	@ExceptionHandler( Exception.class )
	public void handlerException(
			HttpServletRequest request,
			HttpServletResponse response,
			Exception e
			) throws ServletException, IOException {
		//1. 로깅(파일, DB)
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		//log.error( errors.toString() );
		
		//2. 사과 페이지 안내
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("exception", errors.toString());
//		mav.setViewName("error/exception");
//		return mav; 
		
		/* 브라우저에서 네트워크 통신부분을 살펴보면 여러 값들을 주고 받는데, 그 중 header에서 "accept"를 가져옴 */
		String accept = request.getHeader("accept");
		/* 가져온 accept가 application/json 타입이면 아래처럼 처리 */
		if(accept.matches(".*application/json.*") ) {
			/* 실패 JSON 응답 */
			/* Jackson에서 제공해주는 api로 JSON형태로 변환해서 전송 가능하게 해 준다. */
			/* ...@ResponseBody를 써서 return하면 안되는지...??? */
			/* 실패케이스...??? */
			JSONResult jsonResult = JSONResult.fail(errors.toString());
			String json = new ObjectMapper().writeValueAsString(jsonResult);
			
			response.setContentType( "application/json; charset=utf-8" );
			response.getWriter().print( json );
		} else if(request.getRequestURL().toString().contains("/admin")) {
			request.setAttribute("exception", errors.toString());
			request.getRequestDispatcher( "/WEB-INF/views/error/admin_exception.jsp").forward(request, response);
		} else {
			request.setAttribute("exception", errors.toString());
			request.getRequestDispatcher( "/WEB-INF/views/error/exception.jsp").forward(request, response);
		}
	}
}
