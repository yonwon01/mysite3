package com.bigdata2017.mysite.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bigdata2017.mysite.dto.JSONResult;
import com.bigdata2017.mysite.service.UserService;

@RequestMapping( "/api/user" )
@Controller("userAPIController")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping( "/checkemail" )
	public JSONResult checkEmail(@RequestParam(value="email", required=true, defaultValue="") String email) {
		boolean bExist = 
				userService.existUser( email );
		return JSONResult.success( bExist );
	}
}
