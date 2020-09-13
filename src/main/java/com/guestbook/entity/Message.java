package com.guestbook.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

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
@EqualsAndHashCode
@MappedSuperclass
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long messageId;
	private Long userId;
	private Boolean isApproved = Boolean.FALSE;
	private Boolean isDeleted = Boolean.FALSE;

}
