package com.mrtimeey.coronaattendancereportserver.rest.service;

import com.mrtimeey.coronaattendancereportserver.exception.DocumentGenerationException;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.EventTO;
import com.mrtimeey.coronaattendancereportserver.rest.transfer.TeamTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintService {

    private static final DateTimeFormatter SIMPLE_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final PdfService pdfService;
    private final TemplateService templateService;

    public void print(TeamTO teamTO, EventTO eventTO) {
        File tempFile = generateHtmlFile(teamTO, eventTO);
        pdfService.print(tempFile);
    }

    private File generateHtmlFile(TeamTO teamTO, EventTO eventTO) {
        try {
            Map<String, Object> data = getPrintData(teamTO, eventTO);
            String printContent = templateService.processTemplate("print_template", data);
            File tempFile = File.createTempFile("eventContent-", ".html");
            tempFile.deleteOnExit();
            Files.write(tempFile.toPath(), printContent.getBytes(StandardCharsets.UTF_8));
            return tempFile;
        } catch (IOException e) {
            log.error(String.format("Failed generating document! Event: '%s' - Team: '%s'", eventTO, teamTO), e);
            throw new DocumentGenerationException("Failed document generation!", e);
        }
    }

    private Map<String, Object> getPrintData(TeamTO teamTO, EventTO eventTO) {
        Map<String, Object> data = new HashMap<>();
        data.put("eventName", eventTO.getName());
        data.put("teamName", teamTO.getName());
        data.put("participants", eventTO.getParticipants());
        data.put("date", eventTO.getDate().format(SIMPLE_DATE));
        data.put("startTime", eventTO.getStartTime());
        data.put("endTime", eventTO.getEndTime());
        return data;
    }

}

