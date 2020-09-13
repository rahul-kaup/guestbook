package com.guestbook.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MessageBean implements Comparable<MessageBean> {

	private Long messageId;
	private String author;
	private String note;
	private byte[] picture;
	private boolean isApproved;

	@Override
	public int compareTo(MessageBean o) {
		return o.messageId.compareTo(this.messageId);
	}

}
