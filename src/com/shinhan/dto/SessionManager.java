package com.shinhan.dto;

public class SessionManager {
	
	 private static MemberDTO loggedInUser;

	    public static void setLoggedInUser(MemberDTO member) {
	        loggedInUser = member;
	    }

	    public static MemberDTO getLoggedInUser() {
	        return loggedInUser;
	    }

	    public static void logout() {
	        loggedInUser = null;
	    }

}

