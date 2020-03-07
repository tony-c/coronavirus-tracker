package com.tracker.coronavirustracker.services;

import com.tracker.coronavirustracker.models.LocationStats;
import com.tracker.coronavirustracker.utilities.ParseUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {
    private static final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    private List<LocationStats> allStats = new ArrayList<>();
    private int totalCases;
    private int totalPreviousDaysCases;

    public List<LocationStats> getAllStats() {
        return allStats;
    }
    public int getTotalCases() { return totalCases; }
    public int getTotalPreviousDaysCases() { return totalPreviousDaysCases; }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();

            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = ParseUtils.parseIntElseZero(record.get(record.size()-1));
            int previousDayCases = ParseUtils.parseIntElseZero(record.get(record.size()-2));
            totalCases += latestCases;
            totalPreviousDaysCases += previousDayCases;
            locationStat.setNewestTotalCases(latestCases);
            locationStat.setDeltaPreviousDay(latestCases - previousDayCases);
            newStats.add(locationStat);
        }

        System.out.println(totalCases);

        for(LocationStats locationStat : newStats){
            double percentageOfTotal = ((double)locationStat.getNewestTotalCases() / totalCases) * 100.0;
            locationStat.setPercentageOfTotal(percentageOfTotal);
        }
        this.allStats = newStats;
    }
}
