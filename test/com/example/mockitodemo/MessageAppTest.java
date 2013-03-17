package com.example.mockitodemo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.AtMost;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

public class MessageAppTest {

	private static final String INVALIDSERWER = "invalidserwer";
	private static final String INVALIDRECEIPIENT = "invalidreceipient";
	private static final String VALID_CONTENT = "validContent";
	private static final String INVALID_CONTENT = "invalidContent";
	private static final String VALIDRECEIPIENT = "validreceipient";
	private static final String VALIDSERWER = "validserwer";
	MessageService msMock;
	MessageApp ma;
	
	@Before 
	public void setUp(){
		msMock = mock(MessageService.class);
		when(msMock.connect(anyString())).thenReturn(MessageStatus.CONNECTED);
		when(msMock.connect(INVALIDSERWER)).thenReturn(MessageStatus.CONNECTION_ERROR);
		when(msMock.sendMessage(anyString(), anyString())).thenReturn(MessageStatus.SEND);
		when(msMock.sendMessage(eq(INVALIDRECEIPIENT), anyString())).thenReturn(MessageStatus.INVALID_RECEIPIENT);
		when(msMock.sendMessage(anyString(), eq(INVALID_CONTENT))).thenReturn(MessageStatus.INVALID_CONTENT);
		
		ma = new MessageApp(msMock);
	}
	
	@Test
	public void notConnectedSpyTest() {
		MessageService msSpy = spy(new MessageServiceImpl());
		willReturn(MessageStatus.CONNECTION_ERROR).given(msSpy).connect(INVALIDSERWER);
		
		MessageApp ma = new MessageApp(msSpy);
		assertEquals(1, ma.sendMessage(INVALIDSERWER, VALIDRECEIPIENT, VALID_CONTENT));
	}
	
	@Test
	public void InvalidRecipientSpyTest() {
		MessageService msSpy = spy(new MessageServiceImpl());
		willReturn(MessageStatus.INVALID_RECEIPIENT).given(msSpy).sendMessage(eq(INVALIDRECEIPIENT), anyString());	
		
		MessageApp ma = new MessageApp(msSpy);
		assertEquals(4, ma.sendMessage(VALIDSERWER, INVALIDRECEIPIENT, VALID_CONTENT));
	}
	
	@Test
	public void sendSuccesSpyTest() {
		MessageService msSpy = spy(new MessageServiceImpl());
		willReturn(MessageStatus.SEND).given(msSpy).sendMessage(eq(VALIDRECEIPIENT), eq(VALID_CONTENT));	
		
		MessageApp ma = new MessageApp(msSpy);
		assertEquals(0, ma.sendMessage(VALIDSERWER, VALIDRECEIPIENT, VALID_CONTENT));
	}
	
	
	@Test
	public void connectedTest () {	
 		assertEquals(0, ma.sendMessage(VALIDSERWER, VALIDRECEIPIENT, VALID_CONTENT));
 		verify(msMock,times(1)).sendMessage(anyString(), anyString());
	}
	@Test
	public void InvalidRecipientTest(){
		assertEquals(4, ma.sendMessage(VALIDSERWER, INVALIDRECEIPIENT, VALID_CONTENT));
		verify(msMock).sendMessage(anyString(), anyString());	
	}
	@Test
	public void InvalidSerwer() {
		assertEquals(1, ma.sendMessage(INVALIDSERWER, VALIDRECEIPIENT, VALID_CONTENT));
		verify(msMock, never()).sendMessage(anyString(), anyString());
	}
	@Test
	public void InvalidContent () {
		assertEquals(3, ma.sendMessage(VALIDSERWER, VALIDRECEIPIENT, INVALID_CONTENT));
		verify(msMock).sendMessage(anyString(), anyString());
	}
	
	
}
