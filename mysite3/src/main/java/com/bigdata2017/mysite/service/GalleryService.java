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

	public List<GalleryVo> getGalleryList() {
		return galleryDao.getList();
	}

	public boolean saveImageInformation(GalleryVo galleryVo) {
		return galleryDao.insert(galleryVo)==1;
	}

	public boolean deleteImageInformation(Long no) {
		return galleryDao.delete(no)==1;
	}

	public String getGalleryImageURL(Long no) {
		return galleryDao.getImageURL(no);
	}
}
