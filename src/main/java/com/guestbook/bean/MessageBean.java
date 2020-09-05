package com.guestbook.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MessageBean {

	private long messageId;
	private String author;
	private String note;
	private byte[] picture;
	private boolean isApproved;

}
