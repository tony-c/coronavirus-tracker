package com.tracker.coronavirustracker.services;

import com.tracker.coronavirustracker.models.LocationData;
import com.tracker.coronavirustracker.utilities.ParseUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CoronaVirusDataService {
  private static final Logger LOG = LoggerFactory.getLogger(CoronaVirusDataService.class);
  private static final Duration TIMEOUT = Duration.ofSeconds(30);

  private final String virusDataUrl;
  private final HttpClient client = HttpClient.newBuilder().connectTimeout(TIMEOUT).build();

  // Replaced as a whole on each refresh so request threads never see a half-built result
  private volatile Snapshot snapshot = Snapshot.EMPTY;

  public CoronaVirusDataService(@Value("${tracker.data-url}") String virusDataUrl) {
    this.virusDataUrl = virusDataUrl;
  }

  public List<LocationData> getAllStats() {
    return snapshot.allStats;
  }

  public int getTotalCases() {
    return snapshot.totalCases;
  }

  public int getTotalPreviousDaysCases() {
    return snapshot.totalPreviousDaysCases;
  }

  public String getLatestDate() {
    return snapshot.latestDate;
  }

  @PostConstruct
  @Scheduled(cron = "${tracker.refresh-cron}")
  public void fetchVirusData() {
    try {
      HttpResponse<String> httpResponse = getStringHttpResponse();
      if (httpResponse.statusCode() != 200) {
        LOG.warn("Fetching {} returned HTTP {}; keeping previous data", virusDataUrl, httpResponse.statusCode());
        return;
      }
      snapshot = parse(new StringReader(httpResponse.body()));
      LOG.info("Loaded {} locations, data as of {}", snapshot.allStats.size(), snapshot.latestDate);
    } catch (IOException e) {
      LOG.warn("Failed to fetch {}; keeping previous data", virusDataUrl, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  static Snapshot parse(Reader csv) throws IOException {
    List<LocationData> newStats = new ArrayList<>();
    int totalCases = 0;
    int totalPreviousDaysCases = 0;

    CSVParser records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csv);
    List<String> headers = records.getHeaderNames();
    String latestDate = headers.isEmpty() ? "" : headers.get(headers.size() - 1);

    for (CSVRecord record : records) {
      LocationData locationData = new LocationData();

      locationData.setState(record.get("Province/State"));
      locationData.setCountry(record.get("Country/Region"));
      int latestCases = ParseUtils.parseIntElseZero(record.get(record.size() - 1));
      int previousDayCases = ParseUtils.parseIntElseZero(record.get(record.size() - 2));
      totalCases += latestCases;
      totalPreviousDaysCases += previousDayCases;
      locationData.setNewestTotalCases(latestCases);
      locationData.setDeltaPreviousDay(latestCases - previousDayCases);
      newStats.add(locationData);
    }

    for (LocationData stat : newStats) {
      stat.setPercentageOfTotal(totalCases == 0 ? 0 : (double) stat.getNewestTotalCases() / totalCases);
    }

    return new Snapshot(Collections.unmodifiableList(newStats), totalCases, totalPreviousDaysCases, latestDate);
  }

  private HttpResponse<String> getStringHttpResponse() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder(URI.create(virusDataUrl)).timeout(TIMEOUT).build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  static final class Snapshot {
    static final Snapshot EMPTY = new Snapshot(Collections.emptyList(), 0, 0, "");

    final List<LocationData> allStats;
    final int totalCases;
    final int totalPreviousDaysCases;
    final String latestDate;

    Snapshot(List<LocationData> allStats, int totalCases, int totalPreviousDaysCases, String latestDate) {
      this.allStats = allStats;
      this.totalCases = totalCases;
      this.totalPreviousDaysCases = totalPreviousDaysCases;
      this.latestDate = latestDate;
    }
  }
}
