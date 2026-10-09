package com.tracker.coronavirustracker.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParseUtilsTest {

    @Test
    void parsesValidNumbers() {
        assertEquals(42, ParseUtils.parseIntElseZero("42"));
        assertEquals(42, ParseUtils.parseIntElseZero(" 42 "));
    }

    @Test
    void returnsZeroForInvalidInput() {
        assertEquals(0, ParseUtils.parseIntElseZero(null));
        assertEquals(0, ParseUtils.parseIntElseZero(""));
        assertEquals(0, ParseUtils.parseIntElseZero("n/a"));
    }
}
