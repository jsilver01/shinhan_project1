package com.shinhan.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shinhan.DBUtil;
import com.shinhan.dto.BookDTO;
import com.shinhan.dto.BookHistoryDTO;
import com.shinhan.dto.MemberDTO;

public class BookHistoryDAO {
	private final Connection conn = DBUtil.getConnection();

	public boolean isBlackList(int memberId) {
		boolean result = false;
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = """
				select count(*)
				from book_history
				where member_id = ? and return_date < SYSDATE and isreturn = 0
				""";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, memberId);
			rs = st.executeQuery();
			while (rs.next()) {
				int count = rs.getInt(1);
				result = count > 0;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public int borrowBook(BookDTO borrowBook, MemberDTO member) {
		// 책 빌리기
		int result = 0;
		PreparedStatement st = null;
		String sql = """
				insert into book_history(
					bookhistory_id,
					member_id,
					book_id,
					borrow_date,
					return_date,
					isextended,
					isreturn)
				values(book_history_seq.NEXTVAL, ?,?,?,?,?,?)
				""";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberId());
			st.setInt(2, borrowBook.getBookId());
			st.setDate(3, Date.valueOf(LocalDate.now()));
			st.setDate(4, Date.valueOf(LocalDate.now().plusDays(7)));
			st.setBoolean(5, false);
			st.setBoolean(6, false);

			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<BookDTO> selectNonreturn(int memberId) {
		// 반납 안한애들 찾기
		List<BookDTO> nonReturn = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = """
				select *
				from book
				where book_id in (
					select book_id
					from book_history
					where member_id = ? and isreturn = 0)
				""";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, memberId);
			rs = st.executeQuery();
			while (rs.next()) {
				BookDTO book = makeBook(rs);
				nonReturn.add(book);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nonReturn;
	}

	// 관리자 입장에서 반납 안된 책들 다 찾기
	public List<BookDTO> selectAllNonReturn() {
		List<BookDTO> books = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = """
				select *
				from book
				where book_id in (
				select book_id
				from book_history
				where isreturn = 0)
				""";

		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				BookDTO book = makeBook(rs);
				books.add(book);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return books;
	}

	private BookDTO makeBook(ResultSet rs) throws SQLException {
		BookDTO book = BookDTO.builder().author(rs.getString("author")).bookName(rs.getString("book_name"))
				.companyName(rs.getString("company_name")).bookId(rs.getInt("book_id"))
				.createdAt(rs.getDate("created_at")).isbn(rs.getString("isbn")).build();
		return book;
	}

	public int returnBook(int bookId, int memberId) {
		int result = 0;
		PreparedStatement st = null;
		String sql = """
				update book_history
				set isreturn = 1
				where member_id= ? and book_id = ?
				""";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, memberId);
			st.setInt(2, bookId);
			result = st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public List<BookDTO> showBorrowHistory(int memberId) {
		// 해당 유저 대출기록 열람하기 -> memberId 로 다 끌어오기
		List<BookDTO> books = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = """
				select *
				from book
				where book_id in (
					select book_id
					from book_history
					where member_id = ?)
				""";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, memberId);
			rs = st.executeQuery();
			while (rs.next()) {
				BookDTO book = makeBook(rs);
				books.add(book);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return books;
	}

	public List<BookHistoryDTO> selectBorrowing(List<BookDTO> bookList) {
		// bookList 안에 들어있는 bookId 들로 book_history 에서 조회해서 반환하기
		List<BookHistoryDTO> historys = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder("SELECT * FROM book_history WHERE book_id IN (");

		// bookList 크기만큼 ? 추가
		for (int i = 0; i < bookList.size(); i++) {
			sql.append("?");
			if (i < bookList.size() - 1)
				sql.append(",");
		}
		sql.append(")");

		try {
			// 각 bookId 바인딩
			st = conn.prepareStatement(sql.toString());
			for (int i = 0; i < bookList.size(); i++) {
				st.setInt(i + 1, bookList.get(i).getBookId());
			}

			rs = st.executeQuery();
			while (rs.next()) {
				BookHistoryDTO history = makeBookHistory(rs);
				historys.add(history);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return historys;
	}

	private BookHistoryDTO makeBookHistory(ResultSet rs) throws SQLException {
		BookHistoryDTO bookHistory = BookHistoryDTO.builder().bookhistoryId(rs.getInt("bookhistory_id"))
				.bookId(rs.getInt("book_id")).borrowDate(rs.getDate("borrow_date"))
				.isextended(rs.getBoolean("isextended")).isreturn(rs.getBoolean("isreturn"))
				.memberId(rs.getInt("member_id")).returnDate(rs.getDate("return_date")).build();
		return bookHistory;
	}

	public boolean stateCheck(int bookId) {
		boolean result = false;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select count(*) from book_history where book_id = ?";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, bookId);
			rs = st.executeQuery();
			while (rs.next()) {
				int count = rs.getInt(1);
				result = (count > 0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public BookHistoryDTO selectHistoryByBookId(int bookId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		BookHistoryDTO result = null;

		String sql = "select * from book_history where book_id = ?";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, bookId);
			rs = st.executeQuery();

			while (rs.next()) {
				result = makeBookHistory(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public boolean isBorrowing(int bookId) {
		// 현재 빌리고 있는건지 확인하기
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = "select count(*) from book_history where book_id = ? and isreturn = 0";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, bookId);
			rs = st.executeQuery();
			while (rs.next()) {
				int count = rs.getInt(1);
				return (count > 0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public List<BookDTO> showBorrowing(int memberId) {
		List<BookDTO> books = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = """
				select *
				from book
				where book_id in (
					select book_id
					from book_history
					where member_id = ? and isreturn = 0)
				""";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, memberId);
			rs = st.executeQuery();
			while (rs.next()) {
				BookDTO book = makeBook(rs);
				books.add(book);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return books;
	}

	public int extendDays(int bookId, int memberId) {
		PreparedStatement st = null;
		String sql = "update book_history set return_date = return_date + 7 , isextended = 1 where member_id= ? and book_id = ?";
		int result = 0;

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, memberId);
			st.setInt(2, bookId);
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public boolean checkExtended(int bookId, int memberId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select isExtended from book_history where book_id = ? and member_id = ?";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, bookId);
			st.setInt(2, memberId);
			rs = st.executeQuery();

			if (rs.next()) {
				if (rs.getInt(1) == 1) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
