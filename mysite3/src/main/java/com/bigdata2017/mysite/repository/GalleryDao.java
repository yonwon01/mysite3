package com.bigdata2017.mysite.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bigdata2017.mysite.vo.GalleryVo;

@Repository
public class GalleryDao {

	@Autowired
	private SqlSession sqlSession;

	public List<GalleryVo> getList() {
		return sqlSession.selectList("gallery.getList");
	}

	public int insert(GalleryVo galleryVo) {
		return sqlSession.update("gallery.insert", galleryVo);
	}

	public int delete(Long no) {
		return sqlSession.delete("gallery.delete", no);
	}

	public String getImageURL(Long no) {
		return sqlSession.selectOne("gallery.getByNo", no);
	}
	
}
