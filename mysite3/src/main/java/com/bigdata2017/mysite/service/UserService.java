package com.bigdata2017.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigdata2017.mysite.repository.UserDao;
import com.bigdata2017.mysite.vo.UserVo;

/* @Service를 달아 줘야 RootApplicationContext에서 Controller가 요청을 받으면 service를 사용할 수 있게 해준다. 
 * (applicationContext.xml에 service annotation 설정이 되어 있음 */
@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	public boolean existUser(String email) {
		return userDao.get(email) != null;
	}
	
	public boolean join( UserVo userVo ) {
		return userDao.insert(userVo)==1;
	}
	
	public UserVo getUser( String email, String password ) {
		return userDao.get(email, password); 
	}
	
	public UserVo getUser( Long no ) {
		return userDao.get(no);
	}
	
	public boolean modifyUser( UserVo userVo ) {
		return userDao.update(userVo)==1;
	}
}
