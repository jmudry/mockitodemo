package com.example.mockitodemo;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.AtMost;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

public class MessageAppTest {

	/*poprzez Spy*/
	@Test
	public void sendMessageTest () {
		MessageService msSpy = spy(new MessageServiceImpl());
 	//	willReturn(MessageStatus.CONNECTION_ERROR).given(msSpy).connect(anyString());
 	//	willReturn(MessageStatus.SENDING_ERROR).given(msSpy).sendMessage(anyString(), anyString());	
 		willReturn(MessageStatus.SENDING_ERROR).given(msSpy).sendMessage(eq("ksi"), anyString());	
 		//SUT
 		MessageApp ma = new MessageApp(msSpy);
 		
 		//assertEquals(1, ma.sendMessage("sigma", "ksi", "Nie działa wifi"));
 		assertEquals(2, ma.sendMessage("sigma", "ksi", "Nie działa wifi"));
 		assertEquals(0, ma.sendMessage("sigma", "szypulski", "Nie działa wifi"));
 		
	}
	
	@Test
	public void sendMessageTest2 () {
		MessageService msMock = mock(MessageService.class);
	
		when(msMock.connect(anyString())).thenReturn(MessageStatus.CONNECTED);
		when(msMock.connect("sigma")).thenReturn(MessageStatus.CONNECTION_ERROR);
		
		when(msMock.sendMessage(anyString(), anyString())).thenReturn(MessageStatus.SEND);
		when(msMock.sendMessage(eq("ksi"), anyString())).thenReturn(MessageStatus.SENDING_ERROR);
		
 		MessageApp ma = new MessageApp(msMock);
 	
 		assertEquals(1, ma.sendMessage("sigma", "ktos", "Nie działa wifi"));
 		assertEquals(2, ma.sendMessage("manta", "ksi", "Nie działa wifi"));
 		assertEquals(0, ma.sendMessage("delta", "szypulski", "Nie działa wifi"));
 		verify(msMock,times(2)).sendMessage(anyString(), anyString());
 		verify(msMock, atMost(3)).connect(anyString());
 		
	}
	
}
