package com.example.StockServer;

import java.security.Principal;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;

public class WebSocketConnectHandler implements ApplicationListener<SessionConnectEvent> {
	
//	private ActiveWebSocketUserRepository repository;
	private SimpMessageSendingOperations messagingTemplate;

	public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate) {
		super();
		this.messagingTemplate = messagingTemplate;
		//this.repository = repository;
	}
	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		Principal user = SimpMessageHeaderAccessor.getUser(headers);
		if(user == null) {
			return;
		}
		String id = SimpMessageHeaderAccessor.getSessionId(headers);
//		repository.save(new ActiveWebSocketUser(id, user.getName(), Calendar.getInstance()));
//		messagingTemplate.convertAndSend("/topic/friends/signin", Arrays.asList(user.getName()));
	}

	
//	public void onApplicationEvent(ApplicationEvent event) {
//		// TODO Auto-generated method stub
//		
//	}

}
