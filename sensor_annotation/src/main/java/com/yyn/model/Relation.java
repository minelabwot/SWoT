package com.yyn.model;

public class Relation {
	private String name;
	private String link_to;
	
	public Relation(String name, String link_to) {
		super();
		this.name = name;
		this.link_to = link_to;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink_to() {
		return link_to;
	}
	public void setLink_to(String link_to) {
		this.link_to = link_to;
	}
	
}
