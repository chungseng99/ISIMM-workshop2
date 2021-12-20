package com.ftmk.service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public abstract class AbstractViewPDF extends AbstractView{
	
	public AbstractViewPDF() {
	    setContentType("application/pdf");
	  }
	    
	  @Override
	  protected boolean generatesDownloadContent() {
	    return true;
	  }

	  @Override
	  protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    // IE workaround: write into byte array first.
	    ByteArrayOutputStream baos = createTemporaryOutputStream();

	    // Apply preferences and build metadata.
	    PdfWriter writer = new PdfWriter(baos);
	    Document document = new Document(new PdfDocument(writer), PageSize.A4);

	    buildPdfMetadata(model, document, request);

	    // Build PDF document.
	    buildPdfDocument(model, document, writer, request, response);
	    document.close();

	    // Flush to HTTP response.
	    writeToResponse(response, baos);
	  }
	    
	  protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
	  }
	    
	  protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
