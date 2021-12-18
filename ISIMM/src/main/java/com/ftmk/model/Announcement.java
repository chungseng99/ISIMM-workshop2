package com.ftmk.model;

import java.util.Date;

public class Announcement {
	
	private Integer announcementId;
	private String title;
	private String description;
	private Date dateCreated;
	private Integer userId;
	private String name;
	
	public Announcement() {
		
		dateCreated=new Date();
		
	}
	
	public Integer getAnnouncementId() {
		return announcementId;
	}
	public void setAnnouncementId(Integer announcementId) {
		this.announcementId = announcementId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	

}
