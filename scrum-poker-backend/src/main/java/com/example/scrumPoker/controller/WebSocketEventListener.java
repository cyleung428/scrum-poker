package com.example.scrumPoker.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.scrumPoker.model.LocalState;
import com.example.scrumPoker.model.Message;
import com.example.scrumPoker.model.MessageType;



@Component
public class WebSocketEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);
	
	@Autowired
	private SimpMessageSendingOperations sendingOperations;
	
	@EventListener
	public void handleWebSocketConnectListener(final SessionConnectedEvent event) {
		LOGGER.info("Connected");
	}
	
	@EventListener
	public void handleWebSocketDisconnectListner(final SessionDisconnectEvent event) {
		final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		final String username = (String) headerAccessor.getSessionAttributes().get("username");
		LocalState state = LocalState.getInstance();
		state.removeUser(username);
		if (state.checkAllUserSelected()) {
			sendingOperations.convertAndSend("/topic/public",state.getResultMessage());
			state.clearState();
			sendingOperations.convertAndSend("/topic/public",state.getStoryMessage());
			
		}
	}
	
}
