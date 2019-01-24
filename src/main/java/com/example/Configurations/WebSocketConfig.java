package com.example.Configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 
 * @author nikhil.singhal
 *
 */

@Configuration
@ComponentScan({ "com.example.*"})
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*").withSockJS();
    }
    
    
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
//      message
//        .nullDestMatcher().permitAll()
//        .simpDestMatchers("/app/**").hasAnyAuthority(authorities)
//        .simpSubscribeDestMatchers("/topic/**").permitAll()
//        .anyMessage().denyAll();
//      
//      
//      @Override        protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//    	  messages.simpDestMatchers("/user/queue/errors").permitAll() 
//    	  .simpDestMatchers("/admin/**").hasRole("ADMIN").anyMessage()  
//    	  .authenticated();        } 
//     }

     @Override
     protected boolean sameOriginDisabled() {
       return true;
     }   

}