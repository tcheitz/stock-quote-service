package com.thinkhumble.stockquotes.controller;

import com.thinkhumble.stockquotes.model.StockQuote;
import com.thinkhumble.stockquotes.service.StockQuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockQuoteController {

    private final StockQuoteService stockQuoteService;
    @Autowired
    public StockQuoteController(StockQuoteService stockQuoteService) {
        this.stockQuoteService = stockQuoteService;
    }

    @Operation(summary = "Get stock quote by symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stock quote", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid symbol provided", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Stock quote not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/quote")
    public Mono<StockQuote> getQuote(@RequestParam String symbol) {
        return stockQuoteService.getQuote(symbol);
    }

    @Operation(summary = "Get batch stock quotes by symbols")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stock quotes", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid symbols provided", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "One or more stock quotes not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/quotes")
    public Flux<StockQuote> getBatchQuotes(@RequestParam List<String> symbols) {
        return stockQuoteService.getBatchQuotes(symbols);
    }
}
