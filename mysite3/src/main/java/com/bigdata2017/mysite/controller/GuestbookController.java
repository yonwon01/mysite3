package com.bigdata2017.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bigdata2017.mysite.service.GuestbookService;
import com.bigdata2017.mysite.vo.GuestbookVo;

/* guestbook은 class에 붙인 어노테이션 test하려고 잠시 했었음. */
//@Auth	
@Controller
@RequestMapping("/guestbook")
public class GuestbookController {

	@Autowired
	private GuestbookService guestbookService;
	
	@RequestMapping("")
	public String list(Model model) {
		List<GuestbookVo> list = guestbookService.getMessageList();
		
		model.addAttribute("list", list);
		return "guestbook/list";
	}
	
	@RequestMapping("/ajax")
	public String ajax() {
		return "guestbook/index-ajax";
	}
	
	/*/delete/{no} 와 같은 형식으로 하면 jsp에서 {no}로 주소를 넘기면 되니까 주소가 깔끔해짐 */
	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
	public String delete(@PathVariable("no") Long no, Model model) {
		model.addAttribute("no", no);
		return "guestbook/delete";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
//	public String delete(@RequestParam(value="no", required=true, defaultValue="-1") Long no) {
	public String delete(@ModelAttribute GuestbookVo guestbookVo) {
		guestbookService.deleteMessage(guestbookVo);
		return "redirect:/guestbook";
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public String insert(@ModelAttribute GuestbookVo guestbookVo) {
		guestbookService.insertMessage(guestbookVo);
		return "redirect:/guestbook";
	}
}
