package com.shinhan.view;

public class BookHistoryView {

	public void showInsertHistory(int result) {
		System.out.println(result + "건 : 대출이 완료되었습니다.");
	}
	
	public void showReturnHistory(int result) {
		System.out.println(result + "건 : 반납이 완료되었습니다.");
	}
	
	public void selectNum() {
		System.out.print("번호를 입력하세요. >> ");
	}
}
