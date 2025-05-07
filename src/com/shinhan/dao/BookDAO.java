package com.shinhan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.DBUtil;
import com.shinhan.dto.BookDTO;

public class BookDAO {
	private final Connection conn = DBUtil.getConnection();

	public List<BookDTO> showAllBook() {
		List<BookDTO> bookList = new ArrayList<>();
		Statement st = null;
		ResultSet rs = null;

		String sql = "select * from book";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				BookDTO book = makeBook(rs);
				bookList.add(book);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bookList;
	}

	public int bookInsert(BookDTO bookDto) {
		int result = 0;
		PreparedStatement st = null;
		String sql = """
				insert into book(
					book_id,
					isbn,
					book_name,
					author,
					company_name,
					created_at
				) values (book_seq.NEXTVAL, ?,?,?,?,?)
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, bookDto.getIsbn());
			st.setString(2, bookDto.getBookName());
			st.setString(3, bookDto.getAuthor());
			st.setString(4, bookDto.getCompanyName());
			st.setDate(5, bookDto.getCreatedAt());

			result = st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public int deleteBookByIsbn(String isbn) {
		int result = 0;
		PreparedStatement st = null;
		String sql = "delete from book where isbn = ?";

		try {
			st = conn.prepareStatement(sql);
			st.setString(1, isbn);
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<BookDTO> selectBookListByBookName(String bookName) {
		List<BookDTO> bookList = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select * from book where book_name like '%" + bookName + "%'";

		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				BookDTO book = makeBook(rs);
				bookList.add(book);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bookList;
	}

	private BookDTO makeBook(ResultSet rs) throws SQLException {
		BookDTO book = BookDTO.builder().author(rs.getString("author")).bookName(rs.getString("book_name"))
				.companyName(rs.getString("company_name")).bookId(rs.getInt("book_id"))
				.createdAt(rs.getDate("created_at")).isbn(rs.getString("isbn"))
				.build();
		return book;
	}

	public int updateBook(BookDTO newBook) {
		int result = 0;
		PreparedStatement st = null;
		String sql = """
				update book set
					isbn = ?,
					book_name = ?,
					author = ?,
					company_name = ?,
					created_at = ?
				where book_id = ?
				""";
		
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, newBook.getIsbn());
			st.setString(2, newBook.getBookName());
			st.setString(3, newBook.getAuthor());
			st.setString(4, newBook.getCompanyName());
			st.setDate(5, newBook.getCreatedAt());
			st.setInt(6, newBook.getBookId());
			
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}

	public boolean checkAlreadyExists(String isbn) {
		// isbn 으로 이미 존재하는지 아닌지 확인하기 
		boolean result = false;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select count(*) from book where isbn = ?";
		
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, isbn);
			rs = st.executeQuery();
			if(rs.next()) { 
				int count = rs.getInt(1);
			    result = (count > 0); // 개수가 1 이상이면 true
			    return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
