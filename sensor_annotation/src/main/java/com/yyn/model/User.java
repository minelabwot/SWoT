package com.yyn.model;

public class User {
	private int id;
	private String name;
	private String password;
	private String email;
	private String phone;
	private String authority;
	private String alias;
	private String birthday;
	private String sex;
	private String picture;
	public User() {}
	public User(int id, String name, String password, String email, String phone, String authority, String alias,
			String birthday, String sex,String picture) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.authority = authority;
		this.alias = alias;
		this.birthday = birthday;
		this.sex = sex;
		this.picture = picture;
	}
	public String getAlias() {
		return alias;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getAuthority() {
		return authority;
	}
	public String getBirthday() {
		return birthday;
	}
	public String getEmail() {
		return email;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getPhone() {
		return phone;
	}
	public String getSex() {
		return sex;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	
}
