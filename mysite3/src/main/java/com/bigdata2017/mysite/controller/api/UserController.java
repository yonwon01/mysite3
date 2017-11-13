package com.bigdata2017.mysite.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bigdata2017.mysite.dto.JSONResult;
import com.bigdata2017.mysite.service.UserService;

/**
 * com.bigdata2017.mysite.controller.api 패키지는 Jackson lib를 사용해 JSON객체를 응답해 주는 controller만 모아놓게 구조를 잡음
 * String message를 return하는 일반 Controller와 이름이 같아도 무방함
 * Service는 같이 사용할 수 있음
 */
/* UserController가 2개가 있으니 scanning할 때 둘의 id가 모두 "userController"라 에러가 발생할 수 있음. 따라서 아래처럼 Controller에 id를 지정해줌으로 해결가능 */
@Controller("UserAPIController")
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/checkemail")
	public JSONResult checkEmail( @RequestParam(value="email", required=true, defaultValue="") String email ) {
		return JSONResult.success(userService.existUser(email));
	}
}
