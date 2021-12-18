package com.ftmk.model;

public class UserInfo {
	
	private Integer userId;
	private String username;
	private String password;
	private boolean enabled;
	


	public UserInfo(Integer userId, String username, boolean enabled) {
		super();
		this.userId = userId;
		this.username = username;
		this.enabled = enabled;
	}

	public UserInfo(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public UserInfo() {
		
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	

}
