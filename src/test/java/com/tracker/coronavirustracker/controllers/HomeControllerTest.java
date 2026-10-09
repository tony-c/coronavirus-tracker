package com.tracker.coronavirustracker.controllers;

import com.tracker.coronavirustracker.models.LocationData;
import com.tracker.coronavirustracker.services.CoronaVirusDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoronaVirusDataService coronaVirusDataService;

    @DisplayName("Home page renders totals and per-location rows")
    @Test
    void home() throws Exception {
        LocationData canada = new LocationData();
        canada.setCountry("Canada");
        canada.setState("");
        canada.setNewestTotalCases(1500);
        canada.setDeltaPreviousDay(100);
        canada.setPercentageOfTotal(1.0);

        when(coronaVirusDataService.getAllStats()).thenReturn(List.of(canada));
        when(coronaVirusDataService.getTotalCases()).thenReturn(1500);
        when(coronaVirusDataService.getTotalPreviousDaysCases()).thenReturn(1400);
        when(coronaVirusDataService.getLatestDate()).thenReturn("3/9/23");

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("totalReportedCases", 1500))
                .andExpect(model().attribute("totalNewCases", 100))
                .andExpect(content().string(containsString("Canada")))
                .andExpect(content().string(containsString("1,500")))
                .andExpect(content().string(containsString("3/9/23")));
    }
}
