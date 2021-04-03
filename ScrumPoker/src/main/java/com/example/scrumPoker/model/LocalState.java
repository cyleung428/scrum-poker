package com.example.scrumPoker.model;

import java.util.ArrayList;
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
	
	@Getter
	private Map<String, String> storyPointMap = new HashMap<String, String>();

	public void addUser(String username) {
		usernameSet.add(username);
	}

	public void removeUser(String username) {
		usernameSet.remove(username);
		storyPointMap.remove(username);
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
	
	public void clearState() {
		activeID = -1;
		storyMap.values().forEach(story-> {
			story.setActive(false);
		});
		storyPointMap.clear();
	}
	
	public void selectStoryPoint(String username, String storyPoint) throws Exception {
		if (!usernameSet.contains(username)) {
			throw new Exception("Invalid username");
		}else if (activeID == -1) {
			throw new Exception("No active story");
		}else {
			storyPointMap.put(username, storyPoint);
		}
	}
	
	public Boolean checkAllUserSelected() {
		return usernameSet.size() == storyPointMap.size();
	}

	private LocalState() {
		storyMap.put(0, new Story("implement chat feature", 0));
		storyMap.put(1, new Story("add KYC UI to back office", 1));
		storyMap.put(2, new Story("create FX market stop order", 2));
	}

	public static LocalState getInstance() {
		return INSTANCE;
	}
	
	public Message getStoryMessage() {
		Map<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("activeID", activeID);
		ArrayList<Story> storyList = new ArrayList<Story>(storyMap.values());
		contentMap.put("storyList", storyList);
		Message message = Message.builder()
				.type(MessageType.GETSTORY)
				.content(contentMap)
				.build();
		return message;
	}
	
	public Message getResultMessage() {
		Map<String, Object> contentMap = new HashMap<String, Object>();
		Map<String, String> storyPointMapClone = new HashMap<String, String>();
		storyPointMapClone.putAll(storyPointMap);
		contentMap.put("result", storyPointMapClone);
		Message message = Message.builder()
				.type(MessageType.REVEALRESULT)
				.content(contentMap)
				.build();
		return message;
	}
}
