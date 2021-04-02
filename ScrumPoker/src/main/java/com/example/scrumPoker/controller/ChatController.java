package com.example.scrumPoker.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.scrumPoker.model.Message;

@Controller
public class ChatController {
	@MessageMapping("/chat.send")
	@SendTo("/topic/public")
	public Message sendMessage(@Payload final Message chatMessage) {
		return chatMessage;
	}
	
	@MessageMapping("/chat.newUser")
	@SendTo("/topic/public")
	public Message newUser(@Payload final Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		
		return chatMessage;
	}
}
