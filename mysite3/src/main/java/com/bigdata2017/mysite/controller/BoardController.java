package com.bigdata2017.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdata2017.mysite.service.BoardService;
import com.bigdata2017.mysite.vo.BoardVo;
import com.bigdata2017.mysite.vo.UserVo;
import com.bigdata2017.security.Auth;
import com.bigdata2017.security.AuthUser;
import com.bigdata2017.web.util.WebUtil;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@RequestMapping("")
	public String list(
			@RequestParam(value="p", required=true, defaultValue="1") int currentPage,
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword,
			Model model) {
		boardService.getBoardList( keyword, currentPage, model );

		return "board/list";
	}
	
	/* spring에서 추구하는 기술제거를 위해 어노테이션 @Auth가 안붙어 있으면 에러처리하도록 어노테이션 + Interceptor나 AOP를 이용해 막아줄 수 있음.(의미적으로 좋고, 가독성 좋음) */
	@Auth
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write() {
		return "board/write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(
			@AuthUser UserVo authUser,
			@ModelAttribute BoardVo boardVo,
			/* 아래 p, kwd는 게시판 현재 글의 reply를 할 때, 이곳으로 넘어오니 그 때 사용됨 */
			@RequestParam(value="p", required=true, defaultValue="1") Integer page,
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword
			) {
		
		boardVo.setUserNo(authUser.getNo());
		boardService.writeMessage( boardVo );
		
		return "redirect:/board?p=" + page + "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}
	
	@RequestMapping("/view")
	public String view(
			@RequestParam(value="no", required=true, defaultValue="0") Long no,
			@RequestParam(value="p", required=true, defaultValue="1") Integer page,
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword,
			Model model
			) {
		
		BoardVo boardVo = boardService.getMessage(no);
		
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		
		return "board/view";
	}
	
	@Auth
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public String modify(
			@RequestParam(value="no", required=true, defaultValue="0") Long no,
			@RequestParam(value="p", required=true, defaultValue="1") Integer page,
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword,
			Model model
			) {
		BoardVo boardVo = boardService.getMessage(no);
		
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		
		return "/board/modify";
	}
	
	@Auth
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(
			@AuthUser UserVo authUser,
			@ModelAttribute BoardVo boardVo,
			@RequestParam(value="p", required=true, defaultValue="1") Integer page,
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword
			) {
		
		boardVo.setUserNo(authUser.getNo());
		boardService.updateMessage(boardVo);
		return "redirect:/board/view?no=" + boardVo.getNo() + "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}
	
	@Auth
	@RequestMapping("/delete")
	public String delete(
			@AuthUser UserVo authUser,
			@ModelAttribute BoardVo boardVo,
			@RequestParam(value="p", required=true, defaultValue="1") Integer page,
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword
			) {
		
		boardVo.setUserNo(authUser.getNo());
		
		boardService.delete(boardVo);
		return "redirect:/board?p=" + page + "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}
	
	@Auth
	@RequestMapping(value="/reply", method=RequestMethod.GET)
	public String reply(
			@RequestParam(value="no", required=true, defaultValue="0") Long no,
			@RequestParam(value="p", required=true, defaultValue="1") Integer page,
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword,
			Model model
			) {
		BoardVo boardVo = boardService.getMessage(no);
	
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		
		return "board/reply";
	}
}
