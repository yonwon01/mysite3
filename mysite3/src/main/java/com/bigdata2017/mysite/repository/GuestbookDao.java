package com.bigdata2017.mysite.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import com.bigdata2017.mysite.vo.GuestbookVo;

@Repository
public class GuestbookDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int delete( GuestbookVo guestbookVo ) {
		return sqlSession.delete("guestbook.delete", guestbookVo);
	}
	
	public int insert(GuestbookVo guestbookVo) {
		int count = sqlSession.insert("guestbook.insert", guestbookVo);
		return count;
	}

	public List<GuestbookVo> getList() {
		List<GuestbookVo> list = sqlSession.selectList("guestbook.getList");
		return list;
	}

	public List<GuestbookVo> getList(Long no) {
		return sqlSession.selectList("guestbook.getList2", no);
	}

}
