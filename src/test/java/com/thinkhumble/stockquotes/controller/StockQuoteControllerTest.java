package com.thinkhumble.stockquotes.controller;

import com.thinkhumble.stockquotes.model.StockQuote;
import com.thinkhumble.stockquotes.service.StockQuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

@WebFluxTest(StockQuoteController.class)

public class StockQuoteControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private StockQuoteService stockQuoteService;
    private StockQuote stockQuote;

    @BeforeEach
    public void setUp() {
        stockQuote = new StockQuote();
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
    }

    @Test

    public void testGetQuote() {
        when(stockQuoteService.getQuote(anyString())).thenReturn(Mono.just(stockQuote));

        webTestClient.mutateWith(mockUser("user").password("password").roles("USER"))
                .get().uri("/api/stock/quote?symbol=IBM")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.symbol").isEqualTo("IBM")
                .jsonPath("$.price").isEqualTo(135.67);
    }
    @Test
    public void testGetBatchQuotes() {
        List<String> symbols = Arrays.asList("IBM", "AAPL");
        StockQuote stockQuote2 = new StockQuote();
        stockQuote2.setSymbol("AAPL");
        stockQuote2.setPrice(145.67);

        Mockito.when(stockQuoteService.getBatchQuotes(eq(symbols)))
                .thenReturn(Flux.just(stockQuote, stockQuote2));

        webTestClient.mutateWith(mockUser("user").password("password").roles("USER"))
                .get().uri(uriBuilder -> uriBuilder.path("/api/stock/quotes")
                        .queryParam("symbols", String.join(",", symbols))
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].symbol").isEqualTo("IBM")
                .jsonPath("$[1].symbol").isEqualTo("AAPL");
    }
}
