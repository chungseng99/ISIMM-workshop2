package com.ftmk.model;

public class ReportCard {

	
	private Integer reportCardId;
	private Integer userId;
	private Integer resultId;
	private Double attendanceReport;
	private String attitudeReport;
	private String subjectName;
	private Double grade;
	
	public Integer getReportCardId() {
		return reportCardId;
	}
	public void setReportCardId(Integer reportCardId) {
		this.reportCardId = reportCardId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getResultId() {
		return resultId;
	}
	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}
	public Double getAttendanceReport() {
		return attendanceReport;
	}
	public void setAttendanceReport(Double attendanceReport) {
		this.attendanceReport = attendanceReport;
	}
	public String getAttitudeReport() {
		return attitudeReport;
	}
	public void setAttitudeReport(String attitudeReport) {
		this.attitudeReport = attitudeReport;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Double getGrade() {
		return grade;
	}
	public void setGrade(Double grade) {
		this.grade = grade;
	}
	
	
	
}
