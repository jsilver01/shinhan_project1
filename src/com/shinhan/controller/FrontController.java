package com.shinhan.controller;

import java.util.Scanner;

import com.shinhan.view.FrontView;

public class FrontController {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		FrontView frontView = new FrontView();
		boolean isStop = false;
		CommonControllerInterface controller = null;
		while (!isStop) {
			frontView.showMenu();
			String job = sc.nextLine();
			String work = null;
			switch (job.trim()) {
				case "1" -> {
					controller = ControllerFactory.make("register");
					work = "register";
				}
				case "2" -> {
					controller = ControllerFactory.make("login");
					work = "login";
				}
				case "3" -> {
					isStop = true;
					continue;
				}
				default ->{
					System.out.println("올바른 메뉴를 입력해주세요.");
					continue;
				}
			}
			controller.execute(work);
			
		}
		
		sc.close();
		System.out.println("========= 프로그램을 종료합니다. =======");
	}

}
