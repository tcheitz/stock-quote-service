package com.thinkhumble.stockquotes.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockQuoteTest {

    @Test
    public void testStockQuoteGettersAndSetters() {
        StockQuote stockQuote = new StockQuote();
        stockQuote.setSymbol("IBM");
        stockQuote.setPrice(135.67);
        stockQuote.setChange(-1.23);
        stockQuote.setChangePercent(-0.9);
        stockQuote.setTimestamp("2024-07-31");
        stockQuote.setOpen(136.00);
        stockQuote.setHigh(137.00);
        stockQuote.setLow(134.50);
        stockQuote.setVolume(1000000L);
        stockQuote.setPreviousClose(136.90);

        assertEquals("IBM", stockQuote.getSymbol());
        assertEquals(135.67, stockQuote.getPrice());
        assertEquals(-1.23, stockQuote.getChange());
        assertEquals(-0.9, stockQuote.getChangePercent());
        assertEquals("2024-07-31", stockQuote.getTimestamp());
        assertEquals(136.00, stockQuote.getOpen());
        assertEquals(137.00, stockQuote.getHigh());
        assertEquals(134.50, stockQuote.getLow());
        assertEquals(1000000L, stockQuote.getVolume());
        assertEquals(136.90, stockQuote.getPreviousClose());
    }
}