package com.thinkhumble.stockquotes.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.thinkhumble.stockquotes.exception.StockQuoteNotFoundException;
import com.thinkhumble.stockquotes.model.StockQuote;

import java.io.IOException;

public class StockQuoteDeserializer extends JsonDeserializer<StockQuote> {
    @Override
    public StockQuote deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode rootNode = p.getCodec().readTree(p);
        String symbol = rootNode.get("symbol").asText();
        JsonNode node = rootNode.get("Global Quote");
        if (!node.has("01. symbol")) {
            throw new StockQuoteNotFoundException(symbol);
        }
        StockQuote stockQuote = new StockQuote();
        stockQuote.setSymbol(node.get("01. symbol").asText());
        stockQuote.setOpen(node.get("02. open").asDouble());
        stockQuote.setHigh(node.get("03. high").asDouble());
        stockQuote.setLow(node.get("04. low").asDouble());
        stockQuote.setPrice(node.get("05. price").asDouble());
        stockQuote.setVolume(node.get("06. volume").asLong());
        stockQuote.setTimestamp(node.get("07. latest trading day").asText());
        stockQuote.setPreviousClose(node.get("08. previous close").asDouble());
        stockQuote.setChange(node.get("09. change").asDouble());
        stockQuote.setChangePercent(Double.parseDouble(node.get("10. change percent").asText().replace("%", "")));
        return stockQuote;
    }
}
