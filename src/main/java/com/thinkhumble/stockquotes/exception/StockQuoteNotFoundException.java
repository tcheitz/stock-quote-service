package com.thinkhumble.stockquotes.exception;

public class StockQuoteNotFoundException extends RuntimeException{
    public StockQuoteNotFoundException(String symbol){
        super("Stock quote not found for symbol: "+symbol);
    }
}
