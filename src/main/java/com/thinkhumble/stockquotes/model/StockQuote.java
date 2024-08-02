package com.thinkhumble.stockquotes.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.thinkhumble.stockquotes.util.StockQuoteDeserializer;
import lombok.Data;

@Data
@JsonDeserialize(using = StockQuoteDeserializer.class)
public class StockQuote {
    private String symbol;
    private Double price;
    private Double change;
    private Double changePercent;
    private String timestamp;
    private Double open;
    private Double high;
    private Double low;
    private Long volume;
    private Double previousClose;

}
