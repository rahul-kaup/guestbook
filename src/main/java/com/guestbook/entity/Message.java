package com.guestbook.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@EqualsAndHashCode
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long messageId;
	private long userId;
	private String note;
	@Lob
	private byte[] image;
	private int isApproved;
	private int isDeleted;

}
