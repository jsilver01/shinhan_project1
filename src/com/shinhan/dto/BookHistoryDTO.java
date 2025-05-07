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
public class BookHistoryDTO {
	private int bookhistoryId;
	private int memberId;
	private int bookId;
	private Date borrowDate;
	private Date returnDate;
	private boolean isextended;
	private boolean isreturn;
}
