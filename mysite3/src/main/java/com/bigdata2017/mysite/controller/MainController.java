package com.bigdata2017.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping( {"/", "/main"} )
	public String main() {
		return "main/index";
	}
	
//	아래는 전부 테스트 코드
	
//	@ResponseBody
//	@RequestMapping( "/hello" )
//	public String hello() {
//		return "안녕하세요";
//	}
	
	/* 아래는 map을 converting할 수 있는 messageConverter가 없을 때 에러날 수 있음 => Jackson HttpMessageConverter를 사용하면 객체를 넘길 수 있음(외부 lib) */
//	@ResponseBody
//	@RequestMapping( "/json" )
//	public Map json() {
//		Map map = new HashMap();
//		map.put("result", "success");
//		map.put("message", null);
//		map.put("data", 10);
//		
//		return map;
//	}
}
