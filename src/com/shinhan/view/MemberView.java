package com.shinhan.view;

public class MemberView {

	public void getId() {
		System.out.print("아이디 : ");
	}

	public void getPassword() {
		System.out.print("비밀번호 : ");
	}

	public void getName() {
		System.out.print("이름 : ");
	}

	public void getGender() {
		System.out.print("성별(M/F): ");
	}

	public void getMemberType() {
		System.out.print("회원 유형(user/admin): ");
	}

	public void showRegisterTitle() {
		System.out.println("========== 회원가입 ==========");
		System.out.println("1. 관리자 회원가입  2. 일반 사용자 회원가입");
		System.out.print(">> ");
	}
	
	public void errorJob() {
		System.out.println("올바른 번호를 입력해주세요.");
	}

	public void showInsertResult(int result) {
		System.out.println(result + "건: 가입이 완료되었습니다.");
	}
	
	public void showUpdateResult(int result) {
		System.out.println(result + "건: 수정이 완료되었습니다.");
	}
	
	public void showLoginTitle() {
		System.out.println("========== 로그인 ==========");
		System.out.println("1. 관리자 로그인  2. 일반 사용자 로그인");
		System.out.print(">> ");
	}
	
	public void showAdminMenu() {
		System.out.println("===== 관리자 메뉴 =====");
		System.out.println("1. 도서등록");
		System.out.println("2. 도서수정");
		System.out.println("3. 도서삭제");
		System.out.println("4. 대출중인 도서확인");
		System.out.println("5. 개인정보수정");
		System.out.println("6. 로그아웃");
		System.out.print(">> ");
	}
	
	public void showUserMenu() {
		System.out.println("===== 사용자 메뉴 =====");
		System.out.println("1. 도서대출");
		System.out.println("2. 도서반납");
		System.out.println("3. 대출연장");
		System.out.println("4. 대출기록열람");
		System.out.println("5. 개인정보수정");
		System.out.println("6. 로그아웃");
		System.out.print(">> ");
	}
	
	public void showPrivateMenu() {
		System.out.println("=== 개인정보 수정하기 ===");
		System.out.println("1. 이름수정");
		System.out.println("2. 성별수정");
		System.out.println("3. 비밀번호 수정");
		System.out.print(">> ");
	}
	
	public void logout() {
		System.out.println("=== 로그아웃 합니다. ===");
	}

	public void showBlackMenu() {
		System.out.println("==== 반납을 하지 않아 정상 이용이 불가합니다. ====");
		System.out.println("1. 도서반납 2. 로그아웃 ");
		System.out.print(">> ");
	}

	public void alreadyExist() {
		System.out.println("이미 존재하는 아이디입니다.");
		
	}

	public void invalidFormat() {
		System.out.println( "아이디는 영문자와 숫자만 가능합니다.");
		
	}
	
}
