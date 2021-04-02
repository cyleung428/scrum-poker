package com.example.scrumPoker.model;

import java.util.HashSet;
import java.util.Set;

public final class LocalState {

    private static final LocalState INSTANCE = new LocalState();
    private Boolean active = false;
    private Set<String> usernameSet = new HashSet<String>();
    
    public void addUser(String username) {
    	usernameSet.add(username);
    }
    
    public void removeUser(String username) {
    	usernameSet.remove(username);
    }

    private LocalState() {}

    public static LocalState getInstance() {
        return INSTANCE;
    }
}
