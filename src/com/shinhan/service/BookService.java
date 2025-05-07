package com.shinhan.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.dao.BookDAO;
import com.shinhan.dao.BookHistoryDAO;
import com.shinhan.dto.BookDTO;
import com.shinhan.dto.BookHistoryDTO;

public class BookService {

	private final BookDAO bookDao = new BookDAO();
	private final BookHistoryDAO bookHistoryDao = new BookHistoryDAO();

	public int bookInsert(String isbn, String bookName, String author, String companyName) {
		BookDTO bookDto = BookDTO.builder().author(author).bookName(bookName).companyName(companyName).isbn(isbn)
				.createdAt(Date.valueOf(LocalDate.now())).build();

		return bookDao.bookInsert(bookDto);
	}

	public List<BookDTO> selectBookListByName(String bookName) {
		List<BookDTO> bookList = bookDao.selectBookListByBookName(bookName);
		return bookList;
	}

	public int deleteBook(BookDTO book) {
		// 대출중인 책이라면 삭제 불가능 
		if(bookHistoryDao.isBorrowing(book.getBookId())) {
			// true 면 빌리고 있는 상태
			return 0;
		}
		
		return bookDao.deleteBookByIsbn(book.getIsbn());
	}

	public void showBookList(List<BookDTO> bookList) {
		// 대출중 여부 보여주려면 book_history 한번 돌아야함
		List<BookHistoryDTO> historys = new ArrayList<>();
		historys = bookHistoryDao.selectBorrowing(bookList);

		for (int i = 0; i < bookList.size(); i++) {
			BookDTO book = bookList.get(i);
			if (!bookHistoryDao.stateCheck(book.getBookId())) {
				System.out.println((i + 1) + ". " + book.getIsbn() + "/" + book.getBookName() + "/" + book.getAuthor()
						+ "/" + book.getCompanyName() + "/" + "대출가능");
			} else {
				BookHistoryDTO history = bookHistoryDao.selectHistoryByBookId(book.getBookId());
				System.out.println((i + 1) + ". " + book.getIsbn() + "/" + book.getBookName() + "/" + book.getAuthor()
						+ "/" + book.getCompanyName() + "/" + (history.isIsreturn() ? "대출가능" : "대여중"));
			}

		}

	}

	public List<BookDTO> showAllBook() {
		return bookDao.showAllBook();
	}

	public int updateBook(BookDTO newBook) {
		return bookDao.updateBook(newBook);
	}

	public boolean checkAlreadyExists(String isbn) {
		// isbn 으로 이미 존재하는 책인지 아닌지 판별하기
		return bookDao.checkAlreadyExists(isbn);
	}

	public void showBorrowBookList(List<BookDTO> bookList) {
		List<BookHistoryDTO> historys = new ArrayList<>();
		// 대출기록보기니까 for 문 돌면서 해당 아이디로 찾으면 대여중, 반납완료 띄우기
		/**
		 * 여기 다시 만들기 
		 */
		
		
		
	}

	public void showBorrowingBookList(List<BookDTO> borrowingBooks) {
		int i = 1;
		for(BookDTO book : borrowingBooks) {
			System.out.println(i + ". " + book.getIsbn() + "/" + book.getBookName() + "/" + book.getAuthor()
			+ "/" + book.getCompanyName() +  "/대여중");
			i++;
		}
		
	}

}
