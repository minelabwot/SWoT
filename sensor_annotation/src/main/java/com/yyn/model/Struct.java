package com.yyn.model;

public class Struct {
	private String name;
	private String link_to;
	private String rName;
	public Struct(String name, String link_to ,String rName) {
		super();
		this.name = name;
		this.link_to = link_to;
		this.rName = rName;
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
	public String getrName() {
		return rName;
	}
	public void setrName(String rName) {
		this.rName = rName;
	}
	
}