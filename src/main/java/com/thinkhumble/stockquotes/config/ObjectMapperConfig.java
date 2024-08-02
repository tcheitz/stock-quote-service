package com.thinkhumble.stockquotes.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thinkhumble.stockquotes.model.StockQuote;
import com.thinkhumble.stockquotes.util.StockQuoteDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(StockQuote.class, new StockQuoteDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
