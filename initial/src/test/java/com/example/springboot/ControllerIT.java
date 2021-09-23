package com.example.springboot;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
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
        Orders order1 = new Orders("Jessica", 12.50, 10, "buy");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].length()").value("1"))
                .andExpect(jsonPath("[1].length()").value("0"));
    }

    @Test
    void shouldPlaceSellOrde() throws Exception {
        Orders order1 = new Orders("Jessica", 12.50, 10, "sell");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].length()").value("0"))
                .andExpect(jsonPath("[1].length()").value("1"));
    }

    @Test
    void amountZero() throws Exception {
        Orders order1 = new Orders("Jessica", 12.50, 0, "sell");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void priceZero() throws Exception {
        Orders order1 = new Orders("Jessica", 0.00, 10, "sell");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void priceCheck() throws Exception {
        Orders order1 = new Orders("Jessica", 0.32 , 10, "sell");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isOk());
    }

    @Test
    void noUsername() throws Exception {
        Orders order1 = new Orders("", 10.00, 10, "sell");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noAction() throws Exception {
        Orders order1 = new Orders("Jessica", 10.00, 10, "");
        this.mockMvc
                .perform(post("/placeOrder").contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validUser() throws Exception{
        User user1=new User("Jessica","Jessica");
        String expectedJson="JessicaJessica";
        MvcResult result=this.mockMvc
                .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user1)))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson=result.getResponse().getContentAsString();
        Assert.assertEquals(expectedJson,actualJson);
    }

    @Test
    void incorrectPassword() throws Exception{
        User user1=new User("Jessica","James");
        String expectedJson="Invalid";
        MvcResult result=this.mockMvc
                .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user1)))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson=result.getResponse().getContentAsString();
        Assert.assertEquals(expectedJson,actualJson);
    }

    @Test
    void invalidUsername() throws Exception {
        User user1=new User("","James");
        this.mockMvc
                .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidPassword() throws Exception {
        User user1=new User("James","");
        this.mockMvc
                .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser() throws Exception {
        User user1=new User("User5","User5");
        String expectedJson="User added";
        MvcResult result=this.mockMvc
                .perform(post("/addUser").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user1)))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson=result.getResponse().getContentAsString();
        Assert.assertEquals(expectedJson,actualJson);
    }

    @Test
    void usernameUsed() throws Exception {
        User user1=new User("User4","User4");
        String expectedJson="Username already taken";
        MvcResult result=this.mockMvc
                .perform(post("/addUser").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user1)))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson=result.getResponse().getContentAsString();
        Assert.assertEquals(expectedJson,actualJson);
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
