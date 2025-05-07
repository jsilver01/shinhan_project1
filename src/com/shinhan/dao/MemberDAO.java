package com.shinhan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.shinhan.DBUtil;
import com.shinhan.dto.Gender;
import com.shinhan.dto.MemberDTO;
import com.shinhan.dto.MemberType;

public class MemberDAO {
	private final Connection conn = DBUtil.getConnection();

	public MemberDTO selectMemberByIdAndPw(String id, String password, MemberType admin) {
		MemberDTO member = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select * from member where id = '" + id + "' and password = '" + password + "'"
				+ " and member_type = '" + admin + "'";

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				member = makeMember(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return member;
	}

	private MemberDTO makeMember(ResultSet rs) throws SQLException {
		MemberDTO member = MemberDTO.builder().createdAt(rs.getDate("created_at"))
				.gender(Gender.valueOf(rs.getString("gender"))).id(rs.getString("id")).memberId(rs.getInt("member_id"))
				.memberType(MemberType.valueOf(rs.getString("member_type"))).name(rs.getString("name"))
				.password(rs.getString("password")).build();
		return member;
	}

	public int memberInsert(MemberDTO member) {
		int result = 0;
		PreparedStatement st = null;
		String sql = """
				insert into member(
					member_id,
					id,
					password,
					name,
					gender,
					member_type,
					created_at
				) values (member_seq.NEXTVAL, ?,?,?,?,?,?)
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, member.getId());
			st.setString(2, member.getPassword());
			st.setString(3, member.getName());
			st.setString(4, String.valueOf(member.getGender()));
			st.setString(5, String.valueOf(member.getMemberType()));
			st.setDate(6, member.getCreatedAt());

			result = st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public int updateMember(MemberDTO loggedInMember) {
		int result = 0;
		PreparedStatement st = null;
		String sql = """
				update member set
					id = ?,
					password = ?,
					name = ?,
					gender = ?,
					member_type = ?,
					created_at = ?
				where member_id = ?
				""";

		try {
			st = conn.prepareStatement(sql);
			st.setString(1, loggedInMember.getId());
			st.setString(2, loggedInMember.getPassword());
			st.setString(3, loggedInMember.getName());
			st.setString(4, String.valueOf(loggedInMember.getGender()));
			st.setString(5, String.valueOf(loggedInMember.getMemberType()));
			st.setDate(6, loggedInMember.getCreatedAt());
			st.setInt(7, loggedInMember.getMemberId());

			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	// 교수님코드
	public static String buildDebugSQL(String sqlTemplate, Object... params) {
		for (Object param : params) {
			String val = (param instanceof String) ? "'" + param + "'" : String.valueOf(param);
			sqlTemplate = sqlTemplate.replaceFirst("\\?", val);
		}
		return sqlTemplate;
	}

	public boolean selectMemberById(String id) {
		boolean result = false;

		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = "select count(*) from member where id = ?";

		try {
			st = conn.prepareStatement(sql);
			st.setString(1, id);
			rs = st.executeQuery();

			while (rs.next()) {
				int count = rs.getInt(1);
				return (count > 0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
