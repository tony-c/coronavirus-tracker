package com.tracker.coronavirustracker.models;

public class LocationStats {
    private String state;
    private String country;
    private int newestTotalCases;
    private int deltaPreviousDay;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNewestTotalCases() {
        return newestTotalCases;
    }

    public void setNewestTotalCases(int newestTotalCases) {
        this.newestTotalCases = newestTotalCases;
    }

    public int getDeltaPreviousDay() {
        return deltaPreviousDay;
    }

    public void setDeltaPreviousDay(int deltaPreviousDay) {
        this.deltaPreviousDay = deltaPreviousDay;
    }

    @Override
    public String toString() {
        return "LocationStats{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", newestTotalCases=" + newestTotalCases +
                ", deltaPreviousDay=" + deltaPreviousDay +
                '}';
    }
}
