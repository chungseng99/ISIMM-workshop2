package com.ftmk.model;

import java.util.Date;

public class Attendance {
	
	private Integer attendanceId;
	private String attendanceName;
	private Date attendanceDate;
	private String name;
	private Integer userId;
	private String status;
	
	public Integer getAttendanceId() {
		return attendanceId;
	}
	public void setAttendanceId(Integer attendanceId) {
		this.attendanceId = attendanceId;
	}
	public String getAttendanceName() {
		return attendanceName;
	}
	public void setAttendanceName(String attendanceName) {
		this.attendanceName = attendanceName;
	}
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
