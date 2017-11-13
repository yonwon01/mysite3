package com.bigdata2017.mysite.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bigdata2017.mysite.dto.JSONResult;
import com.bigdata2017.mysite.service.GuestbookService;
import com.bigdata2017.mysite.vo.GuestbookVo;

@Controller("guestbookAPIController")
@RequestMapping( "/api/guestbook" )
public class GuestbookController {
	
	@Autowired
	private GuestbookService guestbookService;
	
	@ResponseBody
	@RequestMapping("/list")
	public JSONResult list(
		@RequestParam( value="no", required=true, defaultValue="0") Long no
		) {
		List<GuestbookVo> list = guestbookService.getMessageList(no);
		
		return JSONResult.success(list);
	}
	
	@ResponseBody
	@RequestMapping("/insert")
	public JSONResult insert(
		@RequestBody GuestbookVo guestbookVo) {
		guestbookService.insertMessage(guestbookVo);
		return JSONResult.success(guestbookVo);
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public JSONResult delete(
		@ModelAttribute GuestbookVo guestbookVo 
		) {
		boolean bSuccess = 
				guestbookService.deleteMessage(guestbookVo);
		
		return JSONResult.success( bSuccess ? guestbookVo.getNo() : -1 );
	}
	
}
