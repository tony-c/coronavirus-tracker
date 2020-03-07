package com.tracker.coronavirustracker.utilities;

public class ParseUtils {

    public static int parseIntElseZero(String toParse){
        int num = 0;
        try {
            num = Integer.parseInt(toParse);
        }catch(Exception ignored){

        }
        return num;
    }
}
