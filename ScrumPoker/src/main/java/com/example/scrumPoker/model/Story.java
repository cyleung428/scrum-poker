package com.example.scrumPoker.model;

import lombok.Getter;
import lombok.Setter;

public class Story {
	@Getter
	@Setter
	private String storyName;
	
	@Getter
	@Setter
	private int id;
	
	@Getter
	@Setter
	private Boolean active = false;
	
	public Story(String storyName, int id) {
		this.storyName = storyName;
		this.id = id;
	}
}
