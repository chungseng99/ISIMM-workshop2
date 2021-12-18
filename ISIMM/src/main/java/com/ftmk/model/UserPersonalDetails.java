package com.ftmk.model;

import java.sql.Blob;

public class UserPersonalDetails {
	
	private Integer userDetailId;
	private Integer userId;
	private String email;
	private String name;
	private String icNumber;
	private String phoneNumber;
	private String address;
	private String nationality;
	private String ethnicity;
	private String maritalStatus;
	private Blob picture;
	private UserInfo user;
	
		
	public Integer getUserDetailId() {
		return userDetailId;
	}
	public void setUserDetailId(Integer userDetailId) {
		this.userDetailId = userDetailId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public Blob getPicture() {
		return picture;
	}
	public void setPicture(Blob picture) {
		this.picture = picture;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcNumber() {
		return icNumber;
	}
	public void setIcNumber(String icNumber) {
		this.icNumber = icNumber;
	}
	


}
