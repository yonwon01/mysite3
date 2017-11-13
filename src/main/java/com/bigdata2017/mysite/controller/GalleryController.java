package com.bigdata2017.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bigdata2017.mysite.service.FileUploadService;
import com.bigdata2017.mysite.service.GalleryService;
import com.bigdata2017.mysite.vo.GalleryVo;
import com.bigdata2017.security.Auth;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
	
	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private GalleryService galleryService;
	
	@RequestMapping({"", "/index"})
	public String index( Model model ) {
		List<GalleryVo> list = galleryService.getGalleryList();
		model.addAttribute( "list", list );
		return "gallery/index";
	}

	@Auth(role=Auth.Role.ADMIN)	
	@RequestMapping("/delete/{no}")
	public String delete( @PathVariable("no") Long no ) {
		galleryService.deleteImageInformation(no);
		return "redirect:/gallery/index";
	}
	
	@Auth(role=Auth.Role.ADMIN)	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String upload(
		@ModelAttribute GalleryVo galleryVo,
		@RequestParam("file") MultipartFile multipartFile) {
		
		String imageURL = fileUploadService.restore( multipartFile );
		galleryVo.setImageURL(imageURL);
		
		galleryService.saveImageInformation(galleryVo);
		
		return "redirect:/gallery/index";
	}	
}
