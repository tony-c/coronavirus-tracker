package com.tracker.coronavirustracker.utilities;

public final class ParseUtils {

    private ParseUtils() {
    }

    public static int parseIntElseZero(String toParse){
        if (toParse == null) {
            return 0;
        }
        try {
            return Integer.parseInt(toParse.trim());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
