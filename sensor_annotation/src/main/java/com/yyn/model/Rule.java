package com.yyn.model;

import java.util.Set;

public class Rule {
	private Set<String> events;
	private Set<String> actions;
	
	public Rule(Set<String> events, Set<String> actions) {
		super();
		this.events = events;
		this.actions = actions;
	}

	public Set<String> getEvents() {
		return events;
	}

	public void setEvents(Set<String> events) {
		this.events = events;
	}

	public Set<String> getActions() {
		return actions;
	}

	public void setActions(Set<String> actions) {
		this.actions = actions;
	}
	
	
	
}
