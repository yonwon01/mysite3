package com.bigdata2017.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigdata2017.mysite.repository.GuestbookDao;
import com.bigdata2017.mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	
	@Autowired
	private GuestbookDao guestbookDao;
	
	public List<GuestbookVo> getMessageList() {
		return guestbookDao.getList();
	}
	
	public List<GuestbookVo> getMessageList(Long no) {
		return guestbookDao.getList(no);
	}
	
	public boolean insertMessage( GuestbookVo guestbookVo ) {
		return guestbookDao.insert(guestbookVo)==1;
	}
	
	public boolean deleteMessage(GuestbookVo guestbookVo) {
		return guestbookDao.delete(guestbookVo)==1;
	}
}
