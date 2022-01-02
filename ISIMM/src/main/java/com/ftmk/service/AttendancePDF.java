package com.ftmk.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ftmk.model.StudentAttendance;
import com.ftmk.model.UserPersonalDetails;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class AttendancePDF extends AbstractViewPDF {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<StudentAttendance> attendance = (List<StudentAttendance>) model.get("attendance");
		String className= (String) model.get("className");
		UserPersonalDetails user = (UserPersonalDetails) model.get("user");
		int absentCount= (int) model.get("absent");
		Double attendancePercentage= (Double) model.get("percentage");
		
		
		
		Paragraph header = new Paragraph();
		header.add("Attendance").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(30);
		
		Paragraph content=new Paragraph();
		content.add("Name: "+user.getName()+"\n").setFontSize(16);
		content.add("Class: "+className+"\n").setFontSize(16);
		content.add("Attendance Percentage: "+String.format("%.2f",attendancePercentage)+"%\n").setFontSize(16);
		content.add("Number of Days Absent: "+absentCount+"\n\n").setFontSize(16);

		Color bgColour = new DeviceRgb(128,128,128);
		Table table = new Table(new float[] { 2, 4, 4, 4 });
		table.setWidth(UnitValue.createPercentValue(100));
		table.addHeaderCell(new Cell().add(new Paragraph("No")).setBold().setBackgroundColor(bgColour));
		table.addHeaderCell(new Cell().add(new Paragraph("Attendance Name")).setBold().setBackgroundColor(bgColour));
		table.addHeaderCell(new Cell().add(new Paragraph("Date")).setBold().setBackgroundColor(bgColour));
		table.addHeaderCell(new Cell().add(new Paragraph("Status")).setBold().setBackgroundColor(bgColour));
		
		int i=0;
		for(StudentAttendance attendances:attendance) {
			
			int count=i+1;
			String index= String.valueOf(count);
			table.addCell(index);
			table.addCell(attendances.getAttendanceName());
			table.addCell(attendances.getAttendanceDate().toString());
			table.addCell(attendances.getStatus());
			i++;
		}
		
		document.add(header);
		document.add(content);
		document.add(table);

	}

}
