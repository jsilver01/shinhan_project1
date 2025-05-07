package com.shinhan.service;

import java.sql.Date;
import java.time.LocalDate;

import com.shinhan.dao.MemberDAO;
import com.shinhan.dto.Gender;
import com.shinhan.dto.MemberDTO;
import com.shinhan.dto.MemberType;

public class MemberService {
	private final MemberDAO memberDao = new MemberDAO();
	
	public MemberDTO selectMemberByIdAndPw(String id, String password, MemberType admin) {
		return memberDao.selectMemberByIdAndPw(id, password,admin);
	}
	
	public int memberInsert(String id, String password, String name, Gender gender, MemberType type) {
		
		MemberDTO memberDto = MemberDTO.builder()
				.createdAt(Date.valueOf(LocalDate.now()))
				.gender(gender)
				.id(id)
				.password(password)
				.name(name)
				.memberType(type)
				.build();
		
		return memberDao.memberInsert(memberDto);
	}

	public int updateMember(MemberDTO loggedInMember) {
		return memberDao.updateMember(loggedInMember);
	}

	public boolean selectMemberById(String id) {
		// 아이디 중복검사
		return memberDao.selectMemberById(id);
	}
}
