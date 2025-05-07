package com.shinhan.controller;

public class ControllerFactory {

	public static CommonControllerInterface make(String business) {
		CommonControllerInterface controller = null;
		switch (business) {
		case "register" -> {
			controller = new RegisterController();
		}
		case "login" -> {
			controller = new LoginController();
		}
		case "user" -> {
			controller = new UserController();
		}
		case "admin" -> {
			controller = new AdminController();
		}
		}

		return controller;
	}

}
