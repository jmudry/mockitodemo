package com.example.mockitodemo;

public class MessageServiceImpl implements MessageService {

	private String server = null;

	@Override
	public MessageStatus connect(String server) {
		this.server = server;
		if (server != null && server.length() > 2) {
			System.out.println("Connecting to serwer + " + server);
			return MessageStatus.CONNECTED;
		}
		return MessageStatus.CONNECTION_ERROR;
	}

	@Override
	public MessageStatus sendMessage(String to, String content) {
		if (connect(server) == MessageStatus.CONNECTED) {
			System.out.println("Sending " + content + "to " + to);
			return MessageStatus.SEND;
		}
		System.out.println("Sending error");
		return MessageStatus.SENDING_ERROR;
	}
	
		
}
