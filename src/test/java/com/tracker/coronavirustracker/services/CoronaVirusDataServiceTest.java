package com.tracker.coronavirustracker.services;

import com.tracker.coronavirustracker.models.LocationData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoronaVirusDataServiceTest {

    private static final String CSV =
            "Province/State,Country/Region,Lat,Long,3/8/23,3/9/23\n"
                    + ",Afghanistan,33.9,67.7,100,150\n"
                    + "Ontario,Canada,51.2,-85.3,200,250\n";

    @Test
    void parseComputesTotalsAndDeltas() throws IOException {
        CoronaVirusDataService.Snapshot snapshot = CoronaVirusDataService.parse(new StringReader(CSV));

        assertEquals(2, snapshot.allStats.size());
        assertEquals(400, snapshot.totalCases);
        assertEquals(300, snapshot.totalPreviousDaysCases);
        assertEquals("3/9/23", snapshot.latestDate);

        LocationData canada = snapshot.allStats.get(1);
        assertEquals("Ontario", canada.getState());
        assertEquals("Canada", canada.getCountry());
        assertEquals(250, canada.getNewestTotalCases());
        assertEquals(50, canada.getDeltaPreviousDay());
        assertEquals(0.625, canada.getPercentageOfTotal(), 1e-9);
    }

    @Test
    void parsingTwiceDoesNotAccumulateTotals() throws IOException {
        CoronaVirusDataService.parse(new StringReader(CSV));
        CoronaVirusDataService.Snapshot second = CoronaVirusDataService.parse(new StringReader(CSV));

        assertEquals(400, second.totalCases);
    }

    @Test
    void parseHandlesAllZeroData() throws IOException {
        String csv = "Province/State,Country/Region,Lat,Long,3/8/23,3/9/23\n,Nowhere,0,0,0,0\n";

        CoronaVirusDataService.Snapshot snapshot = CoronaVirusDataService.parse(new StringReader(csv));

        assertEquals(0.0, snapshot.allStats.get(0).getPercentageOfTotal());
    }
}
