package com.example.springboot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIT {

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
    void shouldPlaceABuyOrder() throws Exception {
        Order order1 = new Order("Jessica", 12.50, 10, "buy");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].length()").value("1"))
                .andExpect(jsonPath("[1].length()").value("0"));
    }

    @Test
    void shouldPlaceSellOrder() throws Exception {
        Order order1 = new Order("Jessica", 12.50, 10, "sell");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].length()").value("0"))
                .andExpect(jsonPath("[1].length()").value("1"));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
