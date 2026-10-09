package com.tracker.coronavirustracker;

import com.tracker.coronavirustracker.services.CoronaVirusDataService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CoronaVirusTrackerApplicationTests {

    // Mocked so the context test does not depend on network access to GitHub
    @MockBean
    private CoronaVirusDataService coronaVirusDataService;

    @Test
    void contextLoads() {
    }

}
