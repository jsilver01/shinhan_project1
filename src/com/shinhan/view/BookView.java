package com.shinhan.view;

public class BookView {
	
	public void getIsbn() {
		System.out.print("ISBN : ");
	}

	public void getBookName() {
		System.out.print("도서명 : ");
	}
	
	public void getAuthor() {
		System.out.print("작가 : ");
	}
	
	public void getCompanyName() {
		System.out.print("출판사명 : ");
	}
	
	public void showInsertResult(int result) {
		System.out.println(result + "건 : 도서등록이 완료되었습니다.");
		
	}
	
	public void showDeleteResult(int result) {
		System.out.println(result + "건 : 도서삭제가 완료되었습니다.");
		
	}
	
	public void showUpdateResult(int result) {
		System.out.println(result + "건 : 도서수정이 완료되었습니다.");
		
	}
	
	public void selectBookName() {
		System.out.print("검색할 도서명을 입력하세요. >> ");
	}
	
	public void deleteBookName() {
		System.out.print("삭제할 도서명을 입력하세요. >> ");
	}
	
	public void insertNum() {
		System.out.print("번호를 입력하세요. >> ");
	}
	
	public void zeroResult() {
		System.out.println("해당하는 도서가 없습니다.");
	}
	
	public void noReturn() {
		System.out.println("반납할 도서가 없습니다.");
	}
	
	public void noHistory() {
		System.out.println("대출기록이 없습니다.");
	}
	
	public void selectUpdateField() {
		System.out.println("== 수정할 항목을 선택하세요.==");
		System.out.println("1. 도서명");
		System.out.println("2. 작가");
		System.out.println("3. 출판사명");
		System.out.print(">> ");
	}

	public void noBorrowing() {
		System.out.println("대출중인 도서가 없습니다.");
		
	}

	public void finish() {
		System.out.println("일주일 연장되었습니다.");
		
	}
}
