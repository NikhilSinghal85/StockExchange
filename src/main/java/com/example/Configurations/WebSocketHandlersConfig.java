package com.example.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;


import com.example.StockServer.WebSocketConnectHandler;

@Configuration
public class WebSocketHandlersConfig {

//	@Bean
//	public WebSocketConnectHandler webSocketConnectHandler(SimpMessageSendingOperations messagingTemplate) {
//		return new WebSocketConnectHandler(messagingTemplate);
//	}

//	@Bean
//	public WebSocketDisconnectHandler webSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate, ActiveWebSocketUserRepository repository) {
//		return new WebSocketDisconnectHandler(messagingTemplate, repository);
//	}
}
