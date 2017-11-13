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

	public List<GuestbookVo> getMessageList(Long no){
		List<GuestbookVo> list = guestbookDao.getList(no);
		return list;
	}
	
	public List<GuestbookVo> getMessageList(){
		List<GuestbookVo> list = guestbookDao.getList();
		return list;
	}
	
	public boolean insertMessage( GuestbookVo guestbookVo ) {
		int count = guestbookDao.insert(guestbookVo);
		return count == 1;
	}
	
	public boolean deleteMessage( GuestbookVo guestbookVo ) {
		int count = guestbookDao.delete(guestbookVo);
		return count == 1;
	}
}
