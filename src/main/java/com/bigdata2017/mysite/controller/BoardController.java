package com.bigdata2017.mysite.controller;

import java.util.Map;

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
@RequestMapping( "/board" )
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping( "" )
	public String index(
		@RequestParam( value="p", required=true, defaultValue="1") Integer page,
		@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
		Model model ) {
		
		Map<String, Object> map = 
				boardService.getMessageList( page, keyword );
		
		model.addAttribute( "map", map );
		return "board/index";
	}

	@RequestMapping( "/view" )
	public String view(
		@RequestParam( value="no", required=true, defaultValue="0") Long no,
		@RequestParam( value="p", required=true, defaultValue="1") Integer page,
		@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
		Model model ){
		
		BoardVo boardVo = boardService.getMessage(no);
		
		model.addAttribute( "boardVo", boardVo );
		model.addAttribute( "page", page );
		model.addAttribute( "keyword", keyword );

		return "board/view";
	}
	
	@Auth
	@RequestMapping( value="/modify", method=RequestMethod.GET )
	public String modify(
		@RequestParam( value="no", required=true, defaultValue="0") Long no,
		@RequestParam( value="p", required=true, defaultValue="1") Integer page,
		@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
		Model model ){
			
		BoardVo boardVo = boardService.getMessage(no);
			
		model.addAttribute( "boardVo", boardVo );
		model.addAttribute( "page", page );
		model.addAttribute( "keyword", keyword );

		return "board/modify";
	}

	@Auth
	@RequestMapping( value="/reply", method=RequestMethod.GET )
	public String reply(
		@RequestParam( value="no", required=true, defaultValue="0") Long no,
		@RequestParam( value="p", required=true, defaultValue="1") Integer page,
		@RequestParam( value="kwd", required=true, defaultValue="") String keyword,
		Model model ){
			
		BoardVo boardVo = boardService.getMessage(no);
			
		model.addAttribute( "boardVo", boardVo );
		model.addAttribute( "page", page );
		model.addAttribute( "keyword", keyword );

		return "board/reply";
	}
	
	@Auth
	@RequestMapping( value="/modify", method=RequestMethod.POST )
	public String modify(
		@AuthUser UserVo authUser,	
		@ModelAttribute BoardVo boardVo,
		@RequestParam( value="p", required=true, defaultValue="1") Integer page,
		@RequestParam( value="kwd", required=true, defaultValue="") String keyword ){
		
		boardVo.setUserNo( authUser.getNo() );
		boardService.updateMessage(boardVo);
		
		return 
			"redirect:/board/view" +	
			"?no=" + boardVo.getNo() + 
			"&kwd=" + WebUtil.encodeURL( keyword, "UTF-8");
	}
	
	@Auth
	@RequestMapping( "/delete" )
	public String delete(
		@AuthUser UserVo authUser,
		@ModelAttribute BoardVo vo,
		@RequestParam( value="p", required=true, defaultValue="1") Integer page,
		@RequestParam( value="kwd", required=true, defaultValue="") String keyword ){
		
		vo.setUserNo( authUser.getNo() );
		boardService.deleteMessage( vo );
		
		return 
			"redirect:/board" +
			"?p=" + page + 
			"&kwd=" + WebUtil.encodeURL( keyword, "UTF-8");
	}
	
	@Auth
	@RequestMapping( value="/write", method=RequestMethod.GET )
	public String write() {
		return "board/write";
	}
	
	@Auth
	@RequestMapping( value="/write", method=RequestMethod.POST )
	public String write( 
		@AuthUser UserVo authUser, 
		@ModelAttribute BoardVo vo,
		@RequestParam( value="p", required=true, defaultValue="1") Integer page,
		@RequestParam( value="kwd", required=true, defaultValue="") String keyword ){

		vo.setUserNo( authUser.getNo() );
		boardService.writeMessage( vo );
		
		return
			"redirect:/board" +
			"?p=" + page + 
			"&kwd=" + WebUtil.encodeURL( keyword, "UTF-8");
	}
}