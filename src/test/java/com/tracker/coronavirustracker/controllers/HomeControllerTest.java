package com.tracker.coronavirustracker.controllers;

import com.tracker.coronavirustracker.services.CoronaVirusDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        CoronaVirusDataService coronaVirusDataService = mock(CoronaVirusDataService.class);
        homeController = new HomeController(coronaVirusDataService);
    }

    @DisplayName("Test that proper view name is returned for home name")
    @Test
    void home() {
        assertEquals("home", homeController.home(new ExtendedModelMap()));
    }
}
