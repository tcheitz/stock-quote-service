package com.thinkhumble.stockquotes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkhumble.stockquotes.exception.ExternalApiException;
import com.thinkhumble.stockquotes.exception.InvalidSymbolException;
import com.thinkhumble.stockquotes.exception.NetworkException;
import com.thinkhumble.stockquotes.exception.StockQuoteNotFoundException;
import com.thinkhumble.stockquotes.model.StockQuote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class StockQuoteService {
    private WebClient webClient;
    @Value("${api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;
    public void setBaseUrl(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public StockQuoteService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://www.alphavantage.co").build();
        this.objectMapper = objectMapper;
    }

    private void validateSymbol(String symbol) {
        String symbolPattern = "^[A-Z0-9]+(\\.[A-Z]+)?$";
        if (!Pattern.matches(symbolPattern, symbol)) {
            throw new InvalidSymbolException("Invalid stock symbol: " + symbol);
        }
    }

    @Cacheable(value = "stockQuotes", key = "#symbol")
    public Mono<StockQuote> getQuote(String symbol) {
        validateSymbol(symbol);
        return webClient.get().uri(
                        uriBuilder -> uriBuilder
                                .path("/query")
                                .queryParam("function", "GLOBAL_QUOTE")
                                .queryParam("symbol", symbol)
                                .queryParam("interval", "1min")
                                .queryParam("apikey", apiKey)
                                .build()
                ).retrieve().bodyToMono(String.class).flatMap(json -> {
                    try {
                        String modifiedJson = String.format("{\"symbol\":\"%s\",%s", symbol, json.substring(1));
                        StockQuote stockQuote = objectMapper.readValue(modifiedJson, StockQuote.class);
                        if (stockQuote.getSymbol() == null || !stockQuote.getSymbol().equalsIgnoreCase(symbol)) {
                            return Mono.error(new StockQuoteNotFoundException(symbol));
                        }
                        return Mono.just(stockQuote);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new ExternalApiException("Error parsing JSON response: " + e.getMessage()));
                    }
                })
                .onErrorMap(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return new StockQuoteNotFoundException(symbol);
                    } else {
                        return new ExternalApiException("Error fetching stock quote: " + ex.getMessage());
                    }
                })
                .onErrorMap(Exception.class, ex -> {
                    if (ex instanceof StockQuoteNotFoundException) {
                        return ex;
                    }
                    return new NetworkException("Network error: " + ex.getMessage());
                });
    }

    public Flux<StockQuote> getBatchQuotes(List<String> symbols) {
        return Flux.fromIterable(symbols).flatMap(this::getQuote);
    }
}
