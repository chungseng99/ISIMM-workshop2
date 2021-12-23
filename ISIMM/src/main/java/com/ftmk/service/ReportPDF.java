package com.ftmk.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ftmk.model.ReportCard;
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

public class ReportPDF extends AbstractViewPDF{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		UserPersonalDetails user= (UserPersonalDetails) model.get("user");
		String className= (String) model.get("className");
		List<ReportCard> report = (List<ReportCard>) model.get("report");
		String comment= (String) model.get("comment");
		Double attendance= (Double) model.get("attendance");
		
		
		Paragraph header = new Paragraph();
		header.add("Report Card").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(30);
		
		Paragraph content=new Paragraph();
		content.add("Name: "+user.getName()+"\n").setFontSize(16);
		content.add("Class: "+className+"\n").setFontSize(16);
		content.add("Teacher Comment: "+comment+"\n").setFontSize(16);
		content.add("Attendance Percentage: "+String.format("%.2f",attendance)+"%\n").setFontSize(16);
		

		Color bgColour = new DeviceRgb(128,128,128);
		Table table = new Table(new float[] { 2, 4, 4});
		table.setWidth(UnitValue.createPercentValue(100));
		table.addHeaderCell(new Cell().add(new Paragraph("No")).setBold().setBackgroundColor(bgColour));
		table.addHeaderCell(new Cell().add(new Paragraph("Subject")).setBold().setBackgroundColor(bgColour));
		table.addHeaderCell(new Cell().add(new Paragraph("Marks")).setBold().setBackgroundColor(bgColour));
		
		int i=0;
		for(ReportCard reports:report) {
			
			int count=i+1;
			String index= String.valueOf(count);
			table.addCell(index);
			table.addCell(reports.getSubjectName());
			table.addCell(reports.getGrade().toString());
			i++;
		}
		
		document.add(header);
		document.add(content);
		document.add(table);
		
	}

}
