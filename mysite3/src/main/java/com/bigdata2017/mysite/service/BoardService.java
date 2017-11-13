package com.bigdata2017.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bigdata2017.mysite.repository.BoardDao;
import com.bigdata2017.mysite.vo.BoardVo;

@Service
public class BoardService {

	private static final int LIST_SIZE=5; 
	private static final int PAGE_SIZE=5;

	@Autowired
	private BoardDao boardDao;

	public boolean writeMessage(BoardVo boardVo) {
		Integer groupNo = boardVo.getGroupNo();
		
		if( groupNo != null ) {
			Integer orderNo = boardVo.getOrderNo();
			Integer depth = boardVo.getDepth();
			
			boardDao.increaseGroupOrder(groupNo, orderNo);
			boardVo.setOrderNo(orderNo + 1);
			boardVo.setDepth(depth + 1);
		}
		
		return boardDao.insert(boardVo)==1;
	}

	public boolean delete( BoardVo boardVo ) {
		return boardDao.delete(boardVo)==1;
	}

	private int getTotalCount( String keyword ) {
		return boardDao.getTotalCount(keyword);
	}

	private List<BoardVo> getBoardList( String keyword, Integer page, Integer size ){
		return boardDao.getList(keyword, page, size);
	}

	public void getBoardList( String keyword, Integer currentPage, Model model ) {

		//페이징 계산
		int totalCount = getTotalCount(keyword);
		int pageCount = (int) Math.ceil((double)totalCount/LIST_SIZE);
		int blockCount = (int) Math.ceil((double)pageCount/PAGE_SIZE);
		int currentBlock = (int) Math.ceil((double)currentPage/PAGE_SIZE);

		//파라미터 page 값 검증
		if( currentPage < 1 ) {
			currentPage = 1;
			currentBlock = 1;
		} else if( currentPage > pageCount ){
			currentPage = pageCount;
			currentBlock = (int) Math.ceil((double)currentPage/PAGE_SIZE);
		}

		//5. view에서 페이지 리스트를 렌더링 하기위한 데이터 값 계산
		int beginPage = currentBlock == 0 ? 1 : (currentBlock - 1)*PAGE_SIZE + 1;
		int prevPage = ( currentBlock > 1 ) ? ( currentBlock - 1 ) * PAGE_SIZE : 0;
		int nextPage = ( currentBlock < blockCount ) ? currentBlock * PAGE_SIZE + 1 : 0;
		int endPage = ( nextPage > 0 ) ? ( beginPage - 1 ) + LIST_SIZE : pageCount;

		//6. 리스트 가져오기
		List<BoardVo> list = getBoardList( keyword, currentPage, LIST_SIZE );

		//7. request 범위에 저장
		model.addAttribute( "list", list );

		model.addAttribute( "totalCount", totalCount );
		model.addAttribute( "listSize", LIST_SIZE );
		model.addAttribute( "currentPage", currentPage );
		model.addAttribute( "beginPage", beginPage );
		model.addAttribute( "endPage", endPage );
		model.addAttribute( "prevPage", prevPage );
		model.addAttribute( "nextPage", nextPage );
		model.addAttribute( "keyword", keyword );
	}

	public BoardVo getMessage(Long no) {
		BoardVo boardVo = boardDao.get(no); 
		if( boardVo != null ) {
			boardDao.updateHit(no);
		}
		return boardVo;
	}

	public boolean updateMessage(BoardVo boardVo) {
		return boardDao.update(boardVo)==1;
	}

}
