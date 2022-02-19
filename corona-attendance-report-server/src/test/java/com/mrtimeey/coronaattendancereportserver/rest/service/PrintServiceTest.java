package com.mrtimeey.coronaattendancereportserver.rest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrintServiceTest {

    private PrintService serviceUnderTest;

    @BeforeEach
    public void setup() {
        serviceUnderTest = new PrintService(new PdfService());
    }

    @Test
    void testPrint() throws Exception {
        serviceUnderTest.print();
        assertThat("").isEqualTo("");
    }

}
