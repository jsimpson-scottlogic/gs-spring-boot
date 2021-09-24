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
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnEmptyBuyList() throws Exception{
        String accessToken = obtainAccessToken("User1", "Password1");
        this.mockMvc
                .perform(get("/buyOrders")
                        .header("Authorization",accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value("0"));
    }

    @Test
    void shouldReturnEmptySellList() throws Exception{
        String accessToken = obtainAccessToken("User1", "Password1");
        this.mockMvc
                .perform(get("/sellOrders").header("Authorization",accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value("0"));
    }

    @Test
    void shouldPlaceABuyOrder() throws Exception {
        OrderInfo order1 = new OrderInfo( 12.50, 10, "buy");
        String accessToken = obtainAccessToken("User1", "Password1");
        MvcResult result=this.mockMvc
                .perform(post("/placeOrder").header("Authorization",accessToken).contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldPlaceSellOrder() throws Exception {
        OrderInfo order1 = new OrderInfo(12.50, 10, "sell");
        String accessToken = obtainAccessToken("User1", "Password1");
        this.mockMvc
                .perform(post("/placeOrder").header("Authorization",accessToken).contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isOk());
    }

    @Test
    void amountZero() throws Exception {
        OrderInfo order1 = new OrderInfo(12.50, 0, "sell");
        String accessToken = obtainAccessToken("User1", "Password1");
        this.mockMvc
                .perform(post("/placeOrder").header("Authorization",accessToken).contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void priceZero() throws Exception {
        OrderInfo order1 = new OrderInfo( 0.00, 10, "sell");
        String accessToken = obtainAccessToken("User1", "Password1");
        this.mockMvc
                .perform(post("/placeOrder").header("Authorization",accessToken).contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void priceCheck() throws Exception {
        OrderInfo order1 = new OrderInfo( 0.32 , 10, "sell");
        String accessToken = obtainAccessToken("User1", "Password1");
        this.mockMvc
                .perform(post("/placeOrder").header("Authorization",accessToken).contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isOk());
    }

    @Test
    void noAction() throws Exception {
        String accessToken = obtainAccessToken("User1", "Password1");
        Orders order1 = new Orders("Jessica", 10.00, 10, "");
        this.mockMvc
                .perform(post("/placeOrder").header("Authorization",accessToken).contentType(MediaType.APPLICATION_JSON).content(asJsonString(order1)))
                .andExpect(status().isBadRequest());
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

    private String obtainAccessToken(String username, String password) throws Exception {
        User user1=new User("User1","Password1");
        MvcResult result=this.mockMvc
                .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user1)))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson=result.getResponse().getContentAsString();
        return actualJson;
    }
}
