package com.ftmk.service;

import java.text.DecimalFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ftmk.model.Payment;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class PDFView extends AbstractViewPDF {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Payment payment = (Payment) model.get("payment");
		
		Paragraph header = new Paragraph();
		Paragraph header1 = new Paragraph();
		Paragraph footer= new Paragraph();
		header1.add("Integrated School Information Management and Monitoring System \n").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER).setBorderBottom(new SolidBorder(1));
		header.add("Payment Receipt").setBold().setFontSize(25).setTextAlignment(TextAlignment.CENTER);
		Paragraph details = new Paragraph();
		details.setTextAlignment(TextAlignment.LEFT).setMargin(25).setBorderBottom(new SolidBorder(1)).setFontSize(16);
		details.add("Payment Id: "+ payment.getPaymentId()+"\n");
		details.add("Name: "+ payment.getName()+"\n");
		details.add("Fee Id: "+ payment.getFeeId()+"\n");
		details.add("Payment Amount: RM "+String.format("%.2f", payment.getPaymentAmount())+"\n");
		details.add("Paid on "+ payment.getPaymentDate()+"\n");
		
		footer.add("-Thank you for your payment- \n End of Receipt").setTextAlignment(TextAlignment.CENTER);
		
		document.add(header1).setBorderBottom(null);
		document.add(header);
		document.add(details);
		document.add(footer);

	}


}
