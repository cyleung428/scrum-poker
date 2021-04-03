package com.example.scrumPoker.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.example.scrumPoker.model.LocalState;
import com.example.scrumPoker.model.Message;

@Controller
public class ChatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
	
	@Autowired
	private SimpMessageSendingOperations sendingOperations;
	
	
	@MessageMapping("/chat.newUser")
	@SendTo("/topic/public")
	public Message newUser(@Payload final Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		LocalState state = LocalState.getInstance();
		state.addUser(chatMessage.getSender());
		return state.getStoryMessage();
	}
	
	@MessageMapping("/chat.selectStory")
	public void selectStory(@Payload final Message chatMessage) {
		LocalState state = LocalState.getInstance();
		int id = (int) chatMessage.getContent();
		try {
			state.selectStory(id);
			sendingOperations.convertAndSend("/topic/public", state.getStoryMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	@MessageMapping("/chat.selectStoryPoint")
	public void selectStoryPoint(@Payload final Message chatMessage) {
		LocalState state = LocalState.getInstance();
		String storyPoint = (String) chatMessage.getContent();
		try {
			state.selectStoryPoint(chatMessage.getSender(), storyPoint);
			if (state.checkAllUserSelected()) {
				sendingOperations.convertAndSend("/topic/public", state.getResultMessage());
				state.clearState();
				sendingOperations.convertAndSend("/topic/public", state.getStoryMessage());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
