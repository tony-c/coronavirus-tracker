package com.tracker.coronavirustracker.models;

public class LocationData {
    private String state;
    private String country;
    private int newestTotalCases;
    private int deltaPreviousDay;
    private double percentageOfTotal;

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

    public double getPercentageOfTotal() { return percentageOfTotal; }

    public void setPercentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; }

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
