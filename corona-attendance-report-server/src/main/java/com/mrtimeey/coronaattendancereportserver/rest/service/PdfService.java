package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Component
public class PdfService {

    public Optional<File> print(File inputFile) {
        try {
            org.w3c.dom.Document doc = createDocument(inputFile);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            File outputPdf = new File("target/test_" + LocalDateTime.now().format(formatter) + ".pdf");
            writePdf(inputFile, doc, outputPdf);
            return Optional.of(outputPdf);
        } catch (IOException e) {
            log.error("Failed printing!", e);
        }
        return Optional.empty();
    }

    private org.w3c.dom.Document createDocument(File inputHTML) throws IOException {
        Document document = Jsoup.parse(inputHTML, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return new W3CDom().fromJsoup(document);
    }

    private void writePdf(File inputHTML, org.w3c.dom.Document doc, File outputPdf) throws IOException {
        try (OutputStream os = new FileOutputStream(outputPdf)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withFile(outputPdf);
            builder.toStream(os);
            builder.withW3cDocument(doc, inputHTML.toURI().toString());
            builder.run();
        }
    }
}
