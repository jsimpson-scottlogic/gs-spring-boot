package com.example.springboot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIT {

    Matcher matcher;

    @BeforeEach
    public void setUpEach() {
        matcher = new Matcher();

    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnEmptyBuyList() throws Exception{
        this.mockMvc
                .perform(get("/buyOrders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value("0"));
    }

    @Test
    void shouldReturnEmptySellList() throws Exception{
        this.mockMvc
                .perform(get("/sellOrders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value("0"));
    }

    @Test
    void shouldReturnBuyList() throws Exception{
        Order order1= new Order("Jessica",12.50,10,"buy");
        matcher.processOrder(order1);
        this.mockMvc
                .perform(get("/buyOrders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value("1"));
    }




}
