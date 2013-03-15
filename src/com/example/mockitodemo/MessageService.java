package com.example.mockitodemo;

public interface MessageService {

	MessageStatus connect(String server);
	
	MessageStatus sendMessage(String to, String content);
		
}
