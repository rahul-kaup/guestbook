package com.guestbook.bean;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class MessageBeanTest {

	@Test
	void testDescendingSorting() {
		MessageBean bean1 = new MessageBean(Long.valueOf(1L), "author", "note", null, false);
		MessageBean bean2 = new MessageBean(Long.valueOf(2L), "author", "note", null, false);
		MessageBean bean3 = new MessageBean(Long.valueOf(3L), "author", "note", null, false);

		List<MessageBean> messageList = new ArrayList<>();
		messageList.add(bean2);
		messageList.add(bean1);
		messageList.add(bean3);

		Collections.sort(messageList);

		assertEquals(Long.valueOf(3L), messageList.get(0).getMessageId());
		assertEquals(Long.valueOf(2L), messageList.get(1).getMessageId());
		assertEquals(Long.valueOf(1L), messageList.get(2).getMessageId());
	}
}
