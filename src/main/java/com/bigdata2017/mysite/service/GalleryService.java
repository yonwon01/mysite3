package com.bigdata2017.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigdata2017.mysite.repository.GalleryDao;
import com.bigdata2017.mysite.vo.GalleryVo;

@Service
public class GalleryService {
	
	@Autowired
	private GalleryDao galleryDao;
	
	public boolean saveImageInformation(GalleryVo galleryVo) {
		int count = galleryDao.insert( galleryVo );
		return count == 1;
	}
	
	public boolean deleteImageInformation(Long no ) {
		int count = galleryDao.delete( no );
		return count == 1;
	}

	public List<GalleryVo> getGalleryList() {
		return galleryDao.getList();
	}
}
