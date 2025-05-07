package com.shinhan.service;

import java.util.List;

import com.shinhan.dao.BookHistoryDAO;
import com.shinhan.dto.BookDTO;
import com.shinhan.dto.MemberDTO;
import com.shinhan.dto.SessionManager;

public class BookHistoryService {

	private final BookHistoryDAO bookHistoryDao = new BookHistoryDAO();

	public int borrowBook(BookDTO borrowBook) {
		// 책 빌리기
		MemberDTO member = SessionManager.getLoggedInUser();
		return bookHistoryDao.borrowBook(borrowBook, member);
	}

	public List<BookDTO> selectNonreturn(MemberDTO member) {
		// 반납 안한애들 모아보기 
		return bookHistoryDao.selectNonreturn(member.getMemberId());	
	}

	public int returnBook(BookDTO selectedBook, MemberDTO member) {
		// 책 반납하기 
		return bookHistoryDao.returnBook(selectedBook.getBookId(),member.getMemberId());
	}
	public List<BookDTO> selectAllNonReturn(){
		return bookHistoryDao.selectAllNonReturn();
	}

	public List<BookDTO> showBorrowHistory(MemberDTO loggedInMember) {
		// 대출기록보기
		return bookHistoryDao.showBorrowHistory(loggedInMember.getMemberId());
	}

	public boolean isBlackList(int memberId) {
		return bookHistoryDao.isBlackList(memberId);
	}

	public List<BookDTO> showBorrowing(MemberDTO loggedInMember) {
		return bookHistoryDao.showBorrowing(loggedInMember.getMemberId());
	}

	public int extendDays(int bookId, int memberId) {
		if(bookHistoryDao.checkExtended(bookId,memberId)) {
			return 0;
		}
		return bookHistoryDao.extendDays(bookId, memberId);
	}

}
