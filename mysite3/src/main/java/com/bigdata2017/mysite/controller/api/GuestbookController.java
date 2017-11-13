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

@Controller("GuestbookAPIController")
@RequestMapping("/api/guestbook")
public class GuestbookController {
	
	@Autowired
	public GuestbookService guestbookService;
	
	@ResponseBody
	@RequestMapping("/list")
	public JSONResult list(
			/* no값이 0보다 클 때만 방명록 list를 가져올 수 있게 해줌 */
			@RequestParam(value="no", required=true, defaultValue="0") Long no
			) {
		/* no를 주면 5개씩 뽑아서 가져오는 ajax 버전 list */
		List<GuestbookVo> list = guestbookService.getMessageList(no);
		
		return JSONResult.success(list);
	}
	
	/* @ResponseBody를 설정해 놓음으로서 vo객체를 json형태로 Jackson이 변환시켜서 보낼 수 있게 한다 */
	@ResponseBody
	@RequestMapping("/insert")
	public JSONResult insert(
			/* jsp에서 보내는 json형태 그대로 받을 수 있도록 @RequestBody를 사용 */
			@RequestBody GuestbookVo guestbookVo
			) {
		if(guestbookService.insertMessage(guestbookVo)) {
			return JSONResult.success(guestbookVo);
		}
		return JSONResult.fail("ajax-guestbook insert fail");
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public JSONResult delete(
			@ModelAttribute GuestbookVo guestbookVo
			) {
		/* 제대로 삭제했다면 no를 return, 아니면 -1리턴 */
		return JSONResult.success(guestbookService.deleteMessage(guestbookVo)? guestbookVo.getNo() : -1);
	}
}
