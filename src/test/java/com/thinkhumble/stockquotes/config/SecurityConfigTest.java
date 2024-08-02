package com.thinkhumble.stockquotes.config;

import com.thinkhumble.stockquotes.controller.StockQuoteController;
import com.thinkhumble.stockquotes.service.StockQuoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StockQuoteController.class)
@Import(SecurityConfig.class)
public class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockQuoteService stockQuoteService;

    @Test
    public void testSecuredEndpointWithoutAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/stock/quote?symbol=IBM"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testSecuredEndpointWithAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/stock/quote?symbol=IBM"))
                .andExpect(status().isOk());
    }
    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(formLogin().user("user").password("password"))
                .andExpect(status().is3xxRedirection());
    }
}
