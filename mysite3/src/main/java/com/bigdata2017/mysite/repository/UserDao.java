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
	
	/*
	 * OracleDataSource는 oracle JDBC의 connection pool에서 자원을 가져와 사용하기에
	 * close를 하면 안됨(계속 연결 유지한 상태에서 필요시 가져와 사용해야 하니까)
	 * mybatis가 자동으로 해 주고, SqlSession을 제공해줌
	 */
//	@Autowired
//	private OracleDataSource oracleDataSource;
	
	/*
	 * mybatis의 SqlSession을 사용하려면 SqlSessionTemplate을 DI한다.
	 * 나중에 mybatis가 아닌 다른 회사의 SqlSession을 사용하려면 Dao코드를 수정할 필요 없이
	 * applicationContext.xml에서 관리하는 mybatis SqlSessionFactoryBean과 그 bean을 가지고 생성하는 SqlSessionTemplate을 다른 회사것으로 설정을 바꾸면 됨 
	 */
	@Autowired
	private SqlSession sqlSession;
	
	public int update( UserVo userVo ) {
		return sqlSession.update("user.update", userVo );	
	}
	
	public UserVo get( String email ) {
		return sqlSession.selectOne("user.getByEmail", email);
	}
	
	public UserVo get( Long userNo ) {
		return sqlSession.selectOne("user.getByNo", userNo);
	}
	
	public UserVo get( String email, String password ) throws UserDaoException{
		/* 아래와같이 map으로 할 수도 있고, userVo객체를 만들어 넘길수도 있고, Model 객체를 받아서 처리해도 됨(지금은 보여주려고 map을 new하지만 비효율적) */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("password", password);
		
		/* select로 가져오는게 하나만 가져오기때문에 selectOne을 쓰고, 두 개 이상을 가져오면 에러남 */
		UserVo vo = sqlSession.selectOne("user.getByEmailAndPassword", map);
		
		return vo;
	}
	
	public int insert( UserVo vo ) {
		/* sqlSession이 user.xml설정을 읽어 처리하는데, sqlSession.tag이름("namespace.id", 넘길 객체); */
		int count = sqlSession.insert("user.insert", vo);
		return count;
	}	
	

	/*
	 * oracle JDBC의 connection pool을 사용하면서 bean factory(applicationContext.xml)에서 설정으로 관리하게 한다. 
	 */
//	private Connection getConnection() throws SQLException {
//
//		Connection conn = null;
//
//		try {
//			// JDBC 드라이버 로딩(JDBC 클래스 로딩)
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//
//			// Connection 가져오기
//			String url = "jdbc:oracle:thin:@localhost:1521:xe";
//			conn = DriverManager.getConnection(url, "webdb", "webdb");
//
//		} catch (ClassNotFoundException e) {
//			System.out.println("드라이버 로딩 실패:" + e);
//		}
//
//		return conn;
//	}		
}
