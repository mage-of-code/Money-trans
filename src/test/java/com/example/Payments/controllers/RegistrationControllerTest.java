package com.example.Payments.controllers;

import com.example.Payments.dto.LoginInfo;
import com.example.Payments.services.RegistrationService;
import com.example.Payments.util.validations.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegistrationControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private UserValidator userValidator;
    @MockBean
    private RegistrationService registrationService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void addUserTest() throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName("name");
        loginInfo.setPassword("password");

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/money-trans/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userValidator).validate(eq(loginInfo), any(BindingResult.class));

        verify(registrationService).register(eq(loginInfo));

    }

    @Test
    public void addUserTest_ShouldReturnBadRequest() throws Exception {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName("");
        loginInfo.setPassword("12345678");

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/money-trans/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginInfo)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        verify(userValidator).validate(eq(loginInfo), any(BindingResult.class));
        assertEquals("Размер имени должен быть от 2 до 30 символов;", result.getResolvedException().getMessage());
    }


}
