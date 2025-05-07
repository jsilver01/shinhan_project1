package com.shinhan.controller;

import java.util.List;
import java.util.Scanner;

import com.shinhan.dto.BookDTO;
import com.shinhan.dto.Gender;
import com.shinhan.dto.MemberDTO;
import com.shinhan.dto.MemberType;
import com.shinhan.dto.SessionManager;
import com.shinhan.service.BookHistoryService;
import com.shinhan.service.BookService;
import com.shinhan.service.MemberService;
import com.shinhan.view.BookView;
import com.shinhan.view.MemberView;

public class AdminController implements CommonControllerInterface {

	private final MemberView memberView = new MemberView();
	private final BookView bookView = new BookView();
	private final MemberService memberService = new MemberService();
	private final BookService bookService = new BookService();
	private final BookHistoryService bookHistoryService = new BookHistoryService();
	private final Scanner sc = new Scanner(System.in);

	@Override
	public void execute(String job) {

		switch (job) {
		case "register" -> {
			adminRegister();
		}
		case "login" -> {
			adminLogin();
		}
		case "menu" -> {
			showAdminMenu();
		}
		case "back" -> {
			return;
		}
		}

	}

	private void showAdminMenu() {
		memberView.showAdminMenu();
		String menu = sc.nextLine().trim();
		switch (menu) {
		case "1" -> {
			insertBook();
		}
		case "2" -> {
			updateBook();
		}
		case "3" -> {
			deleteBook();
		}
		case "4" -> {
			showLentedBooks();
		}
		case "5" -> {
			updatePrivate();
		}
		case "6" -> {
			// 로그아웃
			memberView.logout();
			SessionManager.logout();
			execute("back");
			return;
		}
		default -> {
			memberView.errorJob();
		}
		}
		
		execute("menu");
	}

	private void showLentedBooks() {
		System.out.println("=== 대출중인 도서 목록 ===");
		List<BookDTO> lendtedBookList = bookHistoryService.selectAllNonReturn();
		// 위에 리스트에 담겨있는 애들은 아직 반납이 안된 애들
		bookService.showBookList(lendtedBookList);
	}

	private void updatePrivate() {
		memberView.showPrivateMenu();
		String menu = sc.nextLine().trim();
		MemberDTO loggedInMember = SessionManager.getLoggedInUser();

		switch (menu) {
		case "1" -> {
			// 이름수정
			memberView.getName();
			String newName = sc.nextLine().trim();
			while (!newName.matches("^[a-zA-Z가-힣\\s]+$")) {
				memberView.getName();
				newName = sc.nextLine().trim();
			}
			loggedInMember.setName(newName);
		}
		case "2" -> {
			// 성별 수정
			memberView.getGender();
			Gender newGender = Gender.valueOf(sc.nextLine().trim());
			while (!newGender.equals(Gender.F) && !newGender.equals(Gender.M)) {
				memberView.getGender();
				newGender = Gender.valueOf(sc.nextLine().trim().toUpperCase());
			}
			loggedInMember.setGender(newGender);
		}
		case "3" -> {
			// 비밀번호 수정
			memberView.getPassword();
			String newPassword = sc.nextLine().trim();
			while (!newPassword.matches("^[a-zA-Z0-9!~@#$%^&*]+$") || newPassword.length() < 6) {
				memberView.getPassword();
				newPassword = sc.nextLine().trim();
			}
			loggedInMember.setPassword(newPassword);
		}
		}

		int result = memberService.updateMember(loggedInMember);
		memberView.showUpdateResult(result);
	}

	private void deleteBook() {
		System.out.println("=== 도서 삭제하기 ===");
		bookView.deleteBookName();
		String bookName = sc.nextLine().trim();
		while (bookName.length() == 0) {
			bookView.deleteBookName();
			bookName = sc.nextLine().trim();
		}

		List<BookDTO> bookList = bookService.selectBookListByName(bookName);
		bookService.showBookList(bookList);

		bookView.insertNum();
		String num = sc.nextLine();
		BookDTO book = bookList.get(Integer.parseInt(num) - 1);

		int result = bookService.deleteBook(book);
		bookView.showDeleteResult(result);
	}

	private void updateBook() {
		System.out.println("=== 도서 수정하기 ===");
		List<BookDTO> bookList = bookService.showAllBook();
		bookService.showBookList(bookList);

		bookView.insertNum();
		String num = sc.nextLine();
		BookDTO book = bookList.get(Integer.parseInt(num) - 1);

		BookDTO newBook = updateBookInfo(book);
		int result = bookService.updateBook(newBook);
		bookView.showUpdateResult(result);
	}

	private BookDTO updateBookInfo(BookDTO book) {
		BookDTO newBook = null;
		bookView.selectUpdateField();
		String num = sc.nextLine().trim();
		switch (num) {
		case "1" -> {
			bookView.getBookName();
			String bookName = sc.nextLine().trim();
			while (bookName.length() == 0) {
				bookView.getBookName();
				bookName = sc.nextLine().trim();
			}
			book.setBookName(bookName);
		}
		case "2" -> {
			bookView.getAuthor();
			String author = sc.nextLine().trim();
			while (!author.matches("^[a-zA-Z가-힣\\s]+$")) {
				bookView.getAuthor();
				author = sc.nextLine().trim();
			}
			
			book.setAuthor(author);
		}
		case "3" -> {
			bookView.getCompanyName();
			String companyName = sc.nextLine().trim();
			while (!companyName.matches("^[a-zA-Z가-힣\\s]+$")) {
				bookView.getCompanyName();
				companyName = sc.nextLine().trim();
			}

			
			book.setCompanyName(companyName);
		}
		}
		newBook = book;
		return newBook;
	}

	private void insertBook() {
		System.out.println("=== 도서 등록하기 ===");
		bookView.getIsbn();
		String isbn = sc.nextLine().trim().replaceAll("-", "");
		while (!(isbn.length() == 10) || !isValidISBN10(isbn)) {
			bookView.getIsbn();
			isbn = sc.nextLine().trim().replace("-", "");
		}
		while(bookService.checkAlreadyExists(isbn)) {
			System.out.println("이미 존재하는 책입니다.");
			bookView.getIsbn();
			isbn = sc.nextLine().trim().replace("-", "");
		}

		bookView.getBookName();
		String bookName = sc.nextLine().trim();
		while (bookName.length() == 0) {
			bookView.getBookName();
			bookName = sc.nextLine().trim();
		}

		bookView.getAuthor();
		String author = sc.nextLine().trim();
		while (!author.matches("^[a-zA-Z가-힣\\s]+$")) {
			bookView.getAuthor();
			author = sc.nextLine().trim();
		}

		bookView.getCompanyName();
		String companyName = sc.nextLine().trim();
		while (!companyName.matches("^[a-zA-Z가-힣\\s]+$")) {
			bookView.getCompanyName();
			companyName = sc.nextLine().trim();
		}

		int result = bookService.bookInsert(isbn, bookName, author, companyName);
		bookView.showInsertResult(result);
	}

	private static boolean isValidISBN10(String isbn) {
		int sum = 0;
		for (int i = 0; i < 9; i++) { // 처음 9자리 숫자 합 계산
			if (!Character.isDigit(isbn.charAt(i)))
				return false;
			sum += (isbn.charAt(i) - '0') * (10 - i);
		}

		// 마지막 자리 처리 (숫자 또는 'X')
		char lastChar = isbn.charAt(9);
		if (lastChar == 'X' || lastChar == 'x') {
			sum += 10;
		} else if (Character.isDigit(lastChar)) {
			sum += (lastChar - '0');
		} else {
			return false;
		}

		return sum % 11 == 0;
	}

	private void adminLogin() {
		System.out.println("====== 관리자 로그인 =====");
		memberView.getId();
		String id = sc.nextLine().trim();
		memberView.getPassword();
		String password = sc.nextLine().trim();

		MemberDTO member = memberService.selectMemberByIdAndPw(id, password, MemberType.ADMIN);

		while (member == null) {
			// 아이디 비밀번호가 안맞다는 뜻
			System.out.println("아이디와 비밀번호를 확인해주세요.");
			memberView.getId();
			id = sc.nextLine().trim();
			memberView.getPassword();
			password = sc.nextLine().trim();
			member = memberService.selectMemberByIdAndPw(id, password, MemberType.ADMIN);
		}

		// 로그인한 객체 저장하기
		SessionManager.setLoggedInUser(member);

		execute("menu");

	}

	private void adminRegister() {
		String id = null;
		while (true) {
		    memberView.getId();
		    id = sc.nextLine().trim();

		    // 형식이 맞지 않으면 계속 입력 받음
		    if (!id.matches("^[a-zA-Z0-9]+$")) {
		        memberView.invalidFormat(); 
		        continue;
		    }

		    // 중복 검사
		    if (memberService.selectMemberById(id)) {
		        memberView.alreadyExist();
		        continue;
		    }

		    break; // 형식도 맞고, 중복도 아니면 루프 탈출
		}
		
		memberView.getPassword();
		String password = sc.nextLine().trim();
		while (!password.matches("^[a-zA-Z0-9!~@#$%^&*]+$") || password.length() < 6) {
			memberView.getPassword();
			password = sc.nextLine().trim();
		}

		memberView.getName();
		String name = sc.nextLine().trim();
		while (!name.matches("^[a-zA-Z가-힣\\s]+$")) {
			memberView.getName();
			name = sc.nextLine().trim();
		}

		memberView.getGender();
		Gender gender = Gender.valueOf(sc.nextLine().trim().toUpperCase());
		while (!gender.equals(Gender.F) && !gender.equals(Gender.M)) {
			memberView.getGender();
			gender = Gender.valueOf(sc.nextLine().trim().toUpperCase());
		}

		memberView.getMemberType();
		MemberType type = MemberType.valueOf(sc.nextLine().trim().toUpperCase());
		while (!type.equals(MemberType.ADMIN)) {
			memberView.getMemberType();
			type = MemberType.valueOf(sc.nextLine().trim().toUpperCase());
		}

		int result = memberService.memberInsert(id, password, name, gender, type);
		memberView.showInsertResult(result);

	}

}
