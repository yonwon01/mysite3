package com.bigdata2017.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bigdata2017.mysite.vo.BoardVo;

@Repository
public class BoardDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int insert( BoardVo boardVo ) {
		return sqlSession.insert( "board.insert", boardVo );
	}
	
	public int delete( BoardVo boardVo ) {
		return sqlSession.delete( "board.delete", boardVo );
	}
	
	public int getTotalCount( String keyword ) {
		return sqlSession.selectOne( "board.getTotalCount", keyword );
	}
	
	public List<BoardVo> getList( String keyword, Integer page, Integer size ) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put( "keyword", keyword );
		map.put( "page", page );
		map.put( "size", size );
		
		return sqlSession.selectList( "board.getList", map );
	}
	
	public int increaseGroupOrder( Integer groupNo, Integer orderNo ){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put( "groupNo", groupNo );
		map.put( "orderNo", orderNo );
		
		return sqlSession.update( "board.increaseGroupNo", map );
	}
	
	public int updateHit( Long no ) {
		return sqlSession.update( "board.updateHit", no );
	}	
	
	public int update( BoardVo boardVo ) {
		return sqlSession.update( "board.update", boardVo );
	}
	
	public BoardVo get( Long no ) {
		return sqlSession.selectOne( "board.getByNo", no );
	}	
}