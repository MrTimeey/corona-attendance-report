package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


@Component
@RequiredArgsConstructor
public class PrintService {

    public void print() throws IOException, DocumentException {
        URL resource = getClass().getClassLoader().getResource("print/print_template.html");
        File input = FileUtils.toFile(resource);
        Document document = new Document();
        File outputFile = new File("test.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(input));
        document.close();
    }

}
