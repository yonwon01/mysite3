package com.bigdata2017.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bigdata2017.mysite.service.FileUploadService;
import com.bigdata2017.mysite.service.GalleryService;
import com.bigdata2017.mysite.vo.GalleryVo;
import com.bigdata2017.security.Auth;
import com.bigdata2017.security.Auth.Role;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
	
	@Autowired
	private GalleryService galleryService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@RequestMapping({"", "/index"})
	public String index(Model model) {
		
		List<GalleryVo> list = galleryService.getGalleryList();
		
		model.addAttribute("list", list);
		return "gallery/index";
	}
	
	@Auth(role=Role.ADMIN)
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String upload(
			@RequestParam("comment") String comments,
			@RequestParam("file") MultipartFile file
			) {
		GalleryVo galleryVo = new GalleryVo();
		galleryVo.setComments(comments);
		galleryVo.setImageURL(fileUploadService.restore(file));
		galleryService.saveImageInformation(galleryVo);
		return "redirect:/gallery/";
	}
	
	@RequestMapping("/delete/{no}")
	public String delete(
			@PathVariable("no") Long no
			) {
		String imgURL = galleryService.getGalleryImageURL(no);
		
		fileUploadService.delete(imgURL);
		
		galleryService.deleteImageInformation(no);
		return "redirect:/gallery/";
	}
}
