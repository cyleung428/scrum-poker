package com.example.scrumPoker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.scrumPoker.model.LocalState;
import com.example.scrumPoker.model.Message;
import com.example.scrumPoker.model.MessageType;
import com.example.scrumPoker.model.Story;

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
		LocalState state = LocalState.getInstance();
		state.addUser(chatMessage.getSender());
		Map<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("activeID", state.getActiveID());
		ArrayList<Story> storyList = new ArrayList<Story>(state.getStoryMap().values());
		contentMap.put("storyList", storyList);
		chatMessage.setType(MessageType.GETSTORY);
		chatMessage.setContent(contentMap);
		return chatMessage;
	}
}
