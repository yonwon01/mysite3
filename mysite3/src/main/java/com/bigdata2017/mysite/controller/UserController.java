package com.bigdata2017.mysite.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bigdata2017.mysite.service.UserService;
import com.bigdata2017.mysite.vo.UserVo;
import com.bigdata2017.security.Auth;
import com.bigdata2017.security.Auth.Role;
import com.bigdata2017.security.AuthUser;

@Controller
@RequestMapping("/user")
public class UserController {

	/* service도 마찬가지로 applicationContext.xml에 설정되어 있으니 autowired로 연결시켜 줘야함 */
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join(
			/*join.jsp에서 input tag대신 springweb lib에서 제공하는 form:input을 썼기 때문에 userVo를 넘겨주지 않으면 페이지 에러가 발생함*/
			@ModelAttribute UserVo userVo
			) {
		return "user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join( 
			@ModelAttribute @Valid UserVo userVo,
			/* BindingResult를 쓰면 @Valid로 userVo객체를 검증한 후, 결과값을 가져온다. */
			BindingResult result
			) {
		
		if( result.hasErrors() ) {
//			List<ObjectError> list = result.getAllErrors();
//			
//			for( ObjectError error: list ) {
//				System.out.println("Object Error:" + error);
//			}
			return "user/join";
		}
		
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping( "/joinsuccess" )
	public String joinSuccess() {
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}

	/* AuthLoginInterceptor에서 로그인 처리를 해 주니 필요없음 */
//	@RequestMapping(value="/login", method=RequestMethod.POST)
//	public String login(
//			HttpSession session,
//			@RequestParam(value="email", required=true, defaultValue="") String email,
//			@RequestParam(value="password", required=true, defaultValue="") String password
//	) {
//		UserVo userVo = userService.getUser(email, password);
//		if(userVo==null) {
//			return "user/login_fail";
//		}
//		
//		// session 처리
//		session.setAttribute("authUser", userVo);
//		
//		return "redirect:/";
//	}
	
	@RequestMapping("/logout")
	public String logout( HttpSession session ) {
		session.removeAttribute("authUser");
		session.invalidate();
		return "redirect:/";
	}
	
	
	/* authUser를 가져와 로그인 확인하는 부분(보안처리)을 filter, interceptor, AOP 3가지 방법을 annotation을 사용해 쓸 수 있음 */
	@Auth(role=Role.USER)
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public String modify(
			@AuthUser UserVo authUser,
			Model model
		) {
		/* @Auth가 있으니 거기서 authUser 검증 */
		if( authUser == null ) {
			return "redirect:/user/login";
		}
		UserVo userVo = userService.getUser(authUser.getNo());
		model.addAttribute("userVo", userVo);
		
		return "user/modify";
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(
			@AuthUser UserVo authUser,
			@ModelAttribute UserVo userVo
			) {
		userVo.setNo(authUser.getNo());
		userService.modifyUser(userVo);
		
		authUser.setName(userVo.getName());
		return "redirect:/";
	}
}
