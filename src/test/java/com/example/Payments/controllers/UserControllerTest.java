package com.example.Payments.controllers;

import com.example.Payments.dto.AccountTypeDTO;
import com.example.Payments.dto.UserDTO;
import com.example.Payments.mapper.UserMapper;
import com.example.Payments.models.Account;
import com.example.Payments.models.AccountType;
import com.example.Payments.models.User;
import com.example.Payments.security.UserDetailsImpl;
import com.example.Payments.services.PaymentService;
import com.example.Payments.services.UserService;
import com.example.Payments.util.Exceptions.PaymentRequestException;
import com.example.Payments.util.validations.PaymentRequestValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private UserMapper userMapper;
    @MockBean
    private Authentication auth;
    @MockBean
    private SecurityContext securityContext;
    @MockBean
    private UserService userService;
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private PaymentRequestValidator paymentRequestValidator;
    @MockBean
    private ModelMapper modelMapper;

    private Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
    private PaymentRequestException e = new PaymentRequestException();
    private User testUser = new User();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        when(auth.getPrincipal()).thenReturn(buildLoggedInUser());
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        testUser.setName("testUserName");

    }
    @Test
    public void createAccount() throws Exception {

        AccountTypeDTO accountTypeDTO = new AccountTypeDTO();

        AccountType accountType = new AccountType();

        when(modelMapper.map(eq(accountTypeDTO), eq(AccountType.class))).thenReturn(accountType);
        Account account = new Account();
        when(userService.createAccount(eq(accountType))).thenReturn(account);

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/money-trans/users/new-account")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accountTypeDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());




        verify(userService).addAccountToUser(eq(testUser),eq(account));


    }

    @Test
    public void getMyInformationTest() throws Exception {

        UserDTO dto = new UserDTO();
        dto.setName(testUser.getName());

        when(userMapper.toDto(eq(testUser))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/users/my-information"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(testUser.getName())));


    }


    @Test
    public void getAllPaymentsByAccountIdTest() throws Exception {

        when(paymentService.findAllByAccountId(eq(pageable), eq(1))).thenReturn(new PageImpl<>(AdminControllerTest.getPaymentsList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/users/1/all-transfers", pageable))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))


                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountNumberOfPayer",
                        is(AdminControllerTest.getPaymentsList().get(0).getAccountNumberOfPayer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount",
                        is(AdminControllerTest.getPaymentsList().get(0).getAmount().intValue())))


                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountNumberOfReceiver",
                        is(AdminControllerTest.getPaymentsList().get(1).getAccountNumberOfReceiver())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].comment",
                        is(AdminControllerTest.getPaymentsList().get(1).getComment())));


        verify(paymentRequestValidator).validate(eq(testUser), eq(1));

    }


    @Test
    public void getSentPaymentsTest() throws Exception {
        when(paymentService.findSent(eq(pageable), eq(1))).thenReturn(new PageImpl<>(AdminControllerTest.getPaymentsList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/users/1/all-transfers", pageable))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))


                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountNumberOfPayer",
                        is(AdminControllerTest.getPaymentsList().get(0).getAccountNumberOfPayer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount",
                        is(AdminControllerTest.getPaymentsList().get(0).getAmount().intValue())))


                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountNumberOfReceiver",
                        is(AdminControllerTest.getPaymentsList().get(1).getAccountNumberOfReceiver())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].comment",
                        is(AdminControllerTest.getPaymentsList().get(1).getComment())));


        verify(paymentRequestValidator).validate(eq(testUser), eq(1));
    }


    @Test
    public void getReceivedPaymentsTest() throws Exception {
        when(paymentService.findReceived(eq(pageable),eq(1))).thenReturn(new PageImpl<>(AdminControllerTest.getPaymentsList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/users/1/all-transfers", pageable))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))


                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountNumberOfPayer",
                        is(AdminControllerTest.getPaymentsList().get(0).getAccountNumberOfPayer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount",
                        is(AdminControllerTest.getPaymentsList().get(0).getAmount().intValue())))


                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountNumberOfReceiver",
                        is(AdminControllerTest.getPaymentsList().get(1).getAccountNumberOfReceiver())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].comment",
                        is(AdminControllerTest.getPaymentsList().get(1).getComment())));


        verify(paymentRequestValidator).validate(eq(testUser), eq(1));
    }


    @Test
    public void getPaymentsByAccountIdTest_ShouldReturnForbidden() throws Exception {

        doThrow(e).when(paymentRequestValidator).validate(eq(testUser),eq( 1));

       MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/users/1/all-transfers"))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();

       assertEquals("Отсутствуют права на данный запрос",result.getResolvedException().getMessage());
    }

    private UserDetailsImpl buildLoggedInUser() {
        return new UserDetailsImpl(testUser);
    }

}