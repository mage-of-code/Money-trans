package com.example.Payments.controllers;

import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.mapper.PaymentMapper;
import com.example.Payments.models.Payment;
import com.example.Payments.services.PaymentService;
import com.example.Payments.util.validations.PaymentValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest
public class MoneyTransControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;
    @MockBean
    private PaymentValidator paymentValidator;
    @MockBean
    private PaymentMapper paymentMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void transferTest() throws Exception {
        Payment payment = new Payment();
        PaymentDTO dto = new PaymentDTO();
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setAccountNumberOfReceiver("someNumber");
        dto.setAccountNumberOfPayer("someNumber");

        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/money-trans/new-transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(paymentValidator).validate(eq(payment),any(BindingResult.class));
        verify(paymentService).newTransfer(eq(payment));

    }
    @Test
    public void transferTest_ShouldReturnBadRequest() throws Exception {
        Payment payment = new Payment();
        PaymentDTO dto = new PaymentDTO();
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setAccountNumberOfReceiver("someNumber");

        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/money-trans/new-transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        verify(paymentValidator).validate(eq(payment),any(BindingResult.class));

        assertEquals("Введите номер счета отправителя;",result.getResolvedException().getMessage());
    }
}
