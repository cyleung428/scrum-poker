package com.example.scrumPoker.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import lombok.Getter;
import lombok.Setter;

public final class LocalState {

	private static final LocalState INSTANCE = new LocalState();

	@Getter
	@Setter
	private int activeID = -1;
	private Set<String> usernameSet = new HashSet<String>();

	@Getter
	private Map<Integer, Story> storyMap = new HashMap<Integer, Story>();

	public void addUser(String username) {
		usernameSet.add(username);
	}

	public void removeUser(String username) {
		usernameSet.remove(username);
	}

	public void selectStory(int id) throws Exception {
		if (activeID != -1) {
			throw new Exception("There is an active story");
		} else if ( id < 0 && id > 2){
			throw new Exception("Invalid ID");
		} else {
			activeID = id;
			Story selectedStory = storyMap.get(id);
			selectedStory.setActive(true);
		}
	}

	private LocalState() {
		storyMap.put(0, new Story("implement chat feature", 0));
		storyMap.put(1, new Story("add KYC UI to back office", 1));
		storyMap.put(2, new Story("create FX market stop order", 2));
	}

	public static LocalState getInstance() {
		return INSTANCE;
	}
}
