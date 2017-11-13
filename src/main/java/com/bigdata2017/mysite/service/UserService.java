package com.bigdata2017.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigdata2017.mysite.repository.UserDao;
import com.bigdata2017.mysite.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public boolean existUser( String email ) {
		UserVo userVo = userDao.get(email);
		return userVo != null;
	}
	
	public boolean join( UserVo userVo ) {
		return userDao.insert( userVo ) == 1;
	}
	
	public UserVo getUser( String email, String password ) {
		UserVo userVo = userDao.get(email, password);
		return userVo;
	}
	
	public UserVo getUser(Long no) {
		return userDao.get(no);
	}
	
	public boolean modifyUser( UserVo userVo ) {
		return userDao.update(userVo) == 1;
	}
}
