package com.bigdata2017.mysite.repository;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bigdata2017.mysite.exception.UserDaoException;
import com.bigdata2017.mysite.vo.UserVo;

@Repository
public class UserDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int update( UserVo userVo ) {
		int count = sqlSession.update("user.update", userVo );
		return count;	
	}
	
	public UserVo get( String email ) {
		return sqlSession.selectOne( "user.getByEmail", email );
	}
	
	public UserVo get( Long userNo ) {
		return sqlSession.selectOne( "user.getByNo", userNo );
	}
	
	public UserVo get( String email, String password ) throws UserDaoException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("password", password);
		UserVo vo = sqlSession.selectOne("user.getByEmailAndPassword", map);
		
		return vo;
	}
	
	public int insert( UserVo vo ) {
		int count = sqlSession.insert("user.insert", vo);
		return count;
	}	
}
