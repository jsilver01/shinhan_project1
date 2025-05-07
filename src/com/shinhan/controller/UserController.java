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
import com.shinhan.view.BookHistoryView;
import com.shinhan.view.BookView;
import com.shinhan.view.MemberView;

public class UserController implements CommonControllerInterface {
	private final MemberView memberView = new MemberView();
	private final BookView bookView = new BookView();
	private final BookHistoryView historyView = new BookHistoryView();
	private final MemberService memberService = new MemberService();
	private final BookService bookService = new BookService();
	private final BookHistoryService bookHistoryService = new BookHistoryService();
	private final Scanner sc = new Scanner(System.in);

	@Override
	public void execute(String job) {

		switch (job) {
		case "register" -> {
			userRegister();
		}
		case "login" -> {
			userLogin();
		}
		case "menu" -> {
			showUserMenu();
		}
		case "back" -> {
			return;
		}
		case "black" -> {
			showBlackMenu();
		}
		}
	}

	private void showBlackMenu() {
		memberView.showBlackMenu();
		MemberDTO loggedInMember = SessionManager.getLoggedInUser();
		String job = sc.nextLine().trim();
		switch(job) {
		case "1" -> {
			// 도서반납 
			returnBook();
			// 블랙리스트인지 다시 확인
			if(bookHistoryService.isBlackList(loggedInMember.getMemberId())) {
				execute("black");
				return;
			}
			execute("menu");
		}
		case "2" -> {
			// 로그아웃 
			memberView.logout();
			SessionManager.logout();
			execute("back");
			return;
		}
		}
	}

	private void showUserMenu() {
		memberView.showUserMenu();
		String job = sc.nextLine().trim();
		switch (job) {
		case "1" -> {
			// 도서대출
			borrowBook();
		}
		case "2" -> {
			// 도서반납
			returnBook();
		}
		case "3" -> {
			// 대출 연장 
			extendDays();
		}
		case "4" -> {
			// 대출기록열람
			showBorrowHistory();
		}
		case "5" -> {
			// 개인정보수정
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

	private void extendDays() {
		// 대출 연장하기
		System.out.println("=== 대출중인 도서 목록 ===");
		MemberDTO loggedInMember = SessionManager.getLoggedInUser();
		List<BookDTO> borrowingBooks = bookHistoryService.showBorrowing(loggedInMember);
		if(borrowingBooks.isEmpty()) {
			bookView.noBorrowing();
			return;
		}
		bookService.showBorrowingBookList(borrowingBooks);
		historyView.selectNum();
		String num = sc.nextLine().trim();
		
		BookDTO selectedBook = borrowingBooks.get(Integer.parseInt(num) - 1);
		if(bookHistoryService.extendDays(selectedBook.getBookId(), loggedInMember.getMemberId())==0) {
			System.out.println("두번 연장할 수 없습니다.");
			return;
		}else {
			bookView.finish();
		}
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

	private void showBorrowHistory() {
		// 대출 기록 보기
		System.out.println("=== 대출 기록 보기 ===");
		MemberDTO loggedInMember = SessionManager.getLoggedInUser();

		List<BookDTO> historyBooks = bookHistoryService.showBorrowHistory(loggedInMember);
		if(historyBooks.isEmpty()) {
			bookView.noHistory();
			return;
		}
		bookService.showBookList(historyBooks);
	}

	private void returnBook() {
		System.out.println("=== 도서 반납하기 ===");
		// 내가 대출한 도서 목록 출력하기 -> history 에서 return =false 인 애들 골라오기 memberId 로
		MemberDTO member = SessionManager.getLoggedInUser();
		List<BookDTO> nonReturnBooks = bookHistoryService.selectNonreturn(member);
		if(nonReturnBooks.isEmpty()) {
			bookView.noReturn();
			return;
		}
		bookService.showBookList(nonReturnBooks);
		
		historyView.selectNum();
		String num = sc.nextLine().trim();

		// 반납하기 -> history 테이블에서 return = true 로 만들기
		BookDTO selectedBook = nonReturnBooks.get(Integer.parseInt(num) - 1);
		int result = bookHistoryService.returnBook(selectedBook, member);
		historyView.showReturnHistory(result);
	}

	private void borrowBook() {
		System.out.println("=== 도서 대출하기 ===");
		bookView.selectBookName();
		String bookName = sc.nextLine().trim();

		while (bookName.length() == 0) {
			bookView.deleteBookName();
			bookName = sc.nextLine().trim();
		}

		List<BookDTO> bookList = bookService.selectBookListByName(bookName);
		if(bookList.isEmpty()) {
			bookView.zeroResult();
			return;
		}
		bookService.showBookList(bookList);

		bookView.insertNum();
		String num = sc.nextLine().trim();
		BookDTO borrowBook = bookList.get(Integer.parseInt(num) - 1);

		int result = bookHistoryService.borrowBook(borrowBook);
		historyView.showInsertHistory(result);
	}

//	private void selectBookByName() {
//		System.out.println("=== 도서 검색하기 ===");
//		bookView.selectBookName();
//		String bookName = sc.nextLine().trim();
//
//		while (bookName.length() == 0) {
//			bookView.selectBookName();
//			bookName = sc.nextLine().trim();
//		}
//
//		List<BookDTO> bookList = bookService.selectBookListByName(bookName);
//		if(bookList.isEmpty()) {
//			bookView.zeroResult();
//			return;
//		}
//		bookService.showBookList(bookList);
//
//	}

	private void userLogin() {
		System.out.println("======사용자 로그인=====");
		memberView.getId();
		String id = sc.nextLine().trim();
		memberView.getPassword();
		String password = sc.nextLine().trim();

		MemberDTO member = memberService.selectMemberByIdAndPw(id, password, MemberType.USER);

		while (member == null) {
			// 아이디 비밀번호가 안맞다는 뜻
			System.out.println("아이디와 비밀번호를 확인해주세요.");
			memberView.getId();
			id = sc.nextLine().trim();
			memberView.getPassword();
			password = sc.nextLine().trim();
			member = memberService.selectMemberByIdAndPw(id, password, MemberType.USER);
		}

		// 로그인한 객체 저장하기
		SessionManager.setLoggedInUser(member);
		// 연체된 사용자인지 확인 
		MemberDTO loggedInMember = SessionManager.getLoggedInUser();
		if(bookHistoryService.isBlackList(loggedInMember.getMemberId())) {
			execute("black");
			return;
		}
		execute("menu");
	}

	private void userRegister() {
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
		while (!type.equals(MemberType.USER)) {
			memberView.getMemberType();
			type = MemberType.valueOf(sc.nextLine().trim().toUpperCase());
		}

		int result = memberService.memberInsert(id, password, name, gender, type);
		memberView.showInsertResult(result);

	}

}
