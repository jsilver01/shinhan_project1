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
public class BookDTO {
	 private int bookId;
	 private String isbn;
	 private String bookName;
	 private String author;
	 private String companyName;
	 private Date createdAt;
}
