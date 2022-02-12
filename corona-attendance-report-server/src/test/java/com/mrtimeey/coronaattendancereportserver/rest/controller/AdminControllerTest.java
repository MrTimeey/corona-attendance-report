package com.mrtimeey.coronaattendancereportserver.rest.controller;

import com.mrtimeey.coronaattendancereportserver.SpringProfiles;
import com.mrtimeey.coronaattendancereportserver.rest.development.DevelopmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@ActiveProfiles(SpringProfiles.NOSECURITY)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevelopmentService developmentService;

    @Test
    void shouldDeleteEverything() throws Exception {
        mockMvc
                .perform(delete("/admin/deleteAll"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        verify(developmentService, times(1)).deleteAll();
    }

}
