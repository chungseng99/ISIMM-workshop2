package com.ftmk.model;

import java.util.Date;
import java.util.UUID;



public class UserConfirmation {
	
	private int tokenID;
	private String confirmationToken;
	private Date createdDate;
	private UserInfo user;
	
	
	
	public UserConfirmation(UserInfo user) {
		
		this.user = user;
		confirmationToken = UUID.randomUUID().toString();
		createdDate = new Date();
		
	}
	public int getTokenID() {
		return tokenID;
	}
	public void setTokenID(int tokenID) {
		this.tokenID = tokenID;
	}
	public String getConfirmationToken() {
		return confirmationToken;
	}
	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}

}
