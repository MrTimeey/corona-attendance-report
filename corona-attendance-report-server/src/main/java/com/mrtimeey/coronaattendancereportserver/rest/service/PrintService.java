package com.mrtimeey.coronaattendancereportserver.rest.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class PrintService {

    private final PdfService pdfService;

    public void print() {
        URL resource = getClass().getClassLoader().getResource("print/print_template.html");
        File inputFile = FileUtils.toFile(resource);
        pdfService.print(inputFile);
    }

}

