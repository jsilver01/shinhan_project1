package com.shinhan.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	private int memberId;
	private String id;
	private String password;
	private String name;
	private Gender gender;
	private MemberType memberType;
	private Date createdAt;
}
