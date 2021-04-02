package com.example.scrumPoker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class Message {
	
	@Getter
	@Setter
	private MessageType type;
	
	@Getter
	@Setter
	private Object content;
	
	@Getter
	@Setter
	private String sender;
	
	
	
}
