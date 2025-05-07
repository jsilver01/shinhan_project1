package com.shinhan.controller;

import java.util.Scanner;

import com.shinhan.service.MemberService;
import com.shinhan.view.MemberView;

public class RegisterController implements CommonControllerInterface {

	private final MemberView view = new MemberView();

	@Override
	public void execute(String work) {
		switch (work) {
		case "register" -> {
			registerMenu();
		}
		}

	}

	private void registerMenu() {
		CommonControllerInterface controller = null;
		Scanner sc = new Scanner(System.in);

		view.showRegisterTitle();
		String job = sc.next();
		switch (job.trim()) {
		case "1" -> {
			controller = ControllerFactory.make("admin");

		}
		case "2" -> {
			controller = ControllerFactory.make("user");
		}
		default -> {
			view.errorJob();
			return; // 잘못된 입력이면 바로 리턴 (FrontController로 돌아감)

		}
		}
		controller.execute("register");

	}

}