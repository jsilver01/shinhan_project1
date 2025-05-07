package com.shinhan.controller;

import java.util.Scanner;

import com.shinhan.service.MemberService;
import com.shinhan.view.MemberView;

public class LoginController implements CommonControllerInterface {
	private final MemberService memberService = new MemberService();
	private final MemberView view = new MemberView();

	@Override
	public void execute(String work) {

		switch (work) {
		case "login" -> {
			loginMenu();
		}
		}

	}

	private void loginMenu() {
		CommonControllerInterface controller = null;
		Scanner sc = new Scanner(System.in);

		view.showLoginTitle();
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
			return;
		}
		}
		controller.execute("login");

	}

}
