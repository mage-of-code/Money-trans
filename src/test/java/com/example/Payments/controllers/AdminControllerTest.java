package com.example.Payments.controllers;


import com.example.Payments.dto.AccountDTO;
import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.dto.UserDTO;
import com.example.Payments.models.Account;
import com.example.Payments.models.AccountType;
import com.example.Payments.models.User;
import com.example.Payments.services.PaymentService;
import com.example.Payments.services.UserService;
import com.example.Payments.util.Exceptions.AccountNotFoundException;
import com.example.Payments.util.Exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@SpringBootTest
public class AdminControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private PaymentService paymentServiceMock;
    @MockBean
    private UserService userServiceMock;

    private Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
    private AccountNotFoundException e = new AccountNotFoundException();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void getUsersTest() throws Exception {

        UserDTO someUser1 = new UserDTO();
        someUser1.setName("someUser1");

        UserDTO someUser2 = new UserDTO();
        someUser2.setName("someUser2");

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123");
        accountDTO.setAccountType("checking");
        accountDTO.setBalance(BigDecimal.valueOf(100));

        someUser1.setAccountDTOSet(Collections.singleton(accountDTO));
        someUser2.setAccountDTOSet(Collections.singleton(accountDTO));

        List<UserDTO> content = new ArrayList<>();
        content.add(someUser1);
        content.add(someUser2);

        when(userServiceMock.findAll(eq(pageable))).thenReturn(new PageImpl<>(content));

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("someUser1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountDTOSet[0].accountNumber", is("123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", is("someUser2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountDTOSet[0].accountType", is("checking")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountDTOSet[0].balance", is(100)));

    }

    @Test
    public void getUserInformationTest() throws Exception {
        User testUser = new User();

        Account account = new Account();
        account.setAccountNumber("123");
        account.setAccountType(new AccountType("checking"));
        account.setBalance(BigDecimal.valueOf(100));

        testUser.setId(1);
        testUser.setAccounts(Collections.singleton(account));
        testUser.setName("testUser");

        when(userServiceMock.findOne(eq(1))).thenReturn(testUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("testUser")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountDTOSet[0].accountNumber", is("123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountDTOSet[0].accountType", is("checking")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountDTOSet[0].balance", is(100)));


    }


    @Test
    public void getUserInformationTest_ShouldReturnNotFound() throws Exception {

        when(userServiceMock.findOne(eq(1))).thenThrow(new UserNotFoundException());

       MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
       assertEquals("Пользователь не найден",result.getResolvedException().getMessage());

    }


    @Test
    public void getAllPaymentsTest() throws Exception {

        List<PaymentDTO> expectedDTOs = getPaymentsList();
        when(paymentServiceMock.findAll(eq(pageable))).thenReturn(new PageImpl<>(expectedDTOs));

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/all-transfers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountNumberOfPayer",
                        is(expectedDTOs.get(0).getAccountNumberOfPayer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount",
                        is(expectedDTOs.get(0).getAmount().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].comment",
                        is(expectedDTOs.get(0).getComment())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountNumberOfReceiver",
                        is(expectedDTOs.get(1).getAccountNumberOfReceiver())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].amount",
                        is(expectedDTOs.get(1).getAmount().intValue())));
    }

    @Test
    public void getAllPaymentsTestByAccountIdTest() throws Exception {
        List<PaymentDTO> expectedDTOs = getPaymentsList();
        when(paymentServiceMock.findAllByAccountId(eq(pageable),eq(1))).thenReturn(new PageImpl<>(expectedDTOs));

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/1/all-transfers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountNumberOfPayer",
                        is(expectedDTOs.get(0).getAccountNumberOfPayer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount",
                        is(expectedDTOs.get(0).getAmount().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].comment",
                        is(expectedDTOs.get(0).getComment())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountNumberOfReceiver",
                        is(expectedDTOs.get(1).getAccountNumberOfReceiver())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].amount",
                        is(expectedDTOs.get(1).getAmount().intValue())));

    }

    @Test
    public void getAllPaymentsTestByAccountId_ShouldReturnNotFound() throws Exception {

        when(paymentServiceMock.findAllByAccountId(eq(pageable),eq( 1))).thenThrow(e);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/{accountId}/all-transfers", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        assertEquals("Запрашиваемый счет не найден",result.getResolvedException().getMessage());
    }

    @Test
    public void getSentPaymentsTest() throws Exception {

        List<PaymentDTO> expectedDTOs = getPaymentsList();
        when(paymentServiceMock.findSent(eq(pageable), eq(1))).thenReturn(new PageImpl<>(expectedDTOs));

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/{accountId}/sent", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountNumberOfPayer",
                        is(expectedDTOs.get(0).getAccountNumberOfPayer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount",
                        is(expectedDTOs.get(0).getAmount().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountNumberOfReceiver",
                        is(expectedDTOs.get(1).getAccountNumberOfReceiver())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].comment",
                        is(expectedDTOs.get(1).getComment())));

    }

    @Test
    public void getSentPaymentsTest_ShouldReturnNotFound() throws Exception {

        when(paymentServiceMock.findSent(eq(pageable), eq(1))).thenThrow(e);

       MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/{accountId}/sent", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        assertEquals("Запрашиваемый счет не найден",result.getResolvedException().getMessage());
    }

    @Test
    public void getReceivedPaymentsTest() throws Exception {
        List<PaymentDTO> ExpectedDTOs = getPaymentsList();
        when(paymentServiceMock.findReceived(eq(pageable), eq(1))).thenReturn(new PageImpl<>(ExpectedDTOs));

        mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/{accountId}/received", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountNumberOfPayer",
                        is(ExpectedDTOs.get(0).getAccountNumberOfPayer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount",
                        is(ExpectedDTOs.get(0).getAmount().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountNumberOfReceiver",
                        is(ExpectedDTOs.get(1).getAccountNumberOfReceiver())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].comment",
                        is(ExpectedDTOs.get(1).getComment())));


    }

    @Test
    public void getReceivedPaymentsTest_ShouldReturnNotFound() throws Exception {


        when(paymentServiceMock.findReceived(eq(pageable), eq(1))).thenThrow(e);

       MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/money-trans/admin/{accountId}/received", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        assertEquals("Запрашиваемый счет не найден",result.getResolvedException().getMessage());
    }

    public static List<PaymentDTO> getPaymentsList() {
        PaymentDTO paymentDTO1 = new PaymentDTO();

        paymentDTO1.setAccountNumberOfPayer("123");
        paymentDTO1.setAccountNumberOfReceiver("321");
        paymentDTO1.setAmount(BigDecimal.valueOf(100));
        paymentDTO1.setComment("this is first some payment");

        PaymentDTO paymentDTO2 = new PaymentDTO();

        paymentDTO2.setAccountNumberOfPayer("234");
        paymentDTO2.setAccountNumberOfReceiver("432");
        paymentDTO2.setAmount(BigDecimal.valueOf(100));
        paymentDTO2.setComment("this is second some payment");

        List<PaymentDTO> content = new ArrayList<>();
        content.add(paymentDTO1);
        content.add(paymentDTO2);

        return content;
    }

}
