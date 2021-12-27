package com.jp.wallet.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jp.wallet.domain.*;
import com.jp.wallet.service.TransactionService;
import com.jp.wallet.service.UserService;
import com.jp.wallet.service.WalletService;
import com.jp.wallet.service.WalletTransactionService;
import com.jp.wallet.util.TransactionStatus;
import com.jp.wallet.util.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@WebMvcTest()
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    WalletTransactionService walletTransactionService;

    @MockBean
    TransactionService transactionService;

    @MockBean
    WalletService walletService;

    @MockBean
    UserService userService;

    @BeforeEach()
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testPerformTransaction() throws Exception {
        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setTransactionType(TransactionType.CREDIT);
        stsTransaction.setUser("jay123");
        stsTransaction.setAmount(30.0);
        stsTransaction.setTransactionId("12asd");

        ObjectMapper mapper = new ObjectMapper();


        StsTransaction wallletTransaction = new StsTransaction();
        wallletTransaction.setTransactionType(TransactionType.CREDIT);
        wallletTransaction.setUser("jay123");
        wallletTransaction.setAmount(30.0);
        wallletTransaction.setTransactionId("12asd");
        wallletTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        when(walletTransactionService.performWalletTransaction(any())).thenReturn(wallletTransaction);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet/transaction")
                .content(mapper.writeValueAsString(stsTransaction)).contentType(MediaType.APPLICATION_JSON)).andReturn();

        StsTransaction stsTransactionResp = mapper.readValue(mvcResult.getResponse().getContentAsString(), StsTransaction.class);
        assertEquals(TransactionStatus.SUCCESS, stsTransactionResp.getTransactionStatus());
    }


    @Test
    public void testBalanceCheckTransaction() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        StsTransaction walletTransaction = new StsTransaction();
        walletTransaction.setTransactionType(TransactionType.CREDIT);
        walletTransaction.setUser("jay123");
        walletTransaction.setAmount(30.0);
        walletTransaction.setTransactionId("12asd");
        walletTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        User user = new User("jay12", "Jay", true);
        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));

        Wallet wallet = new Wallet("ad23",30.0, true, user);
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/wallet/jay12/BALANCE")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        StsTransaction stsTransactionResp = mapper.readValue(mvcResult.getResponse().getContentAsString(), StsTransaction.class);
        assertEquals(TransactionStatus.SUCCESS, stsTransactionResp.getTransactionStatus());
        assertEquals(30.0, stsTransactionResp.getAmount());
    }

    @Test
    public void testBalanceCheckTransactionWithInvalidUser() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        StsTransaction walletTransaction = new StsTransaction();
        walletTransaction.setTransactionType(TransactionType.CREDIT);
        walletTransaction.setUser("jay123");
        walletTransaction.setAmount(30.0);
        walletTransaction.setTransactionId("12asd");
        walletTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        User user = new User("jay12", "Jay", true);
        when(userService.findUserDetails(anyString())).thenReturn(Optional.empty());

        Wallet wallet = new Wallet("ad23",30.0, true, user);
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/wallet/jay12/BALANCE")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        ApiError stsTransactionResp = mapper.readValue(mvcResult.getResponse().getContentAsString(), ApiError.class);
        assertEquals(HttpStatus.FORBIDDEN, stsTransactionResp.getStatus());
        assertEquals("User Details not found!", stsTransactionResp.getMessage());
    }

    @Test
    public void testBalanceCheckTransactionWithInactiveUser() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        StsTransaction walletTransaction = new StsTransaction();
        walletTransaction.setTransactionType(TransactionType.CREDIT);
        walletTransaction.setUser("jay123");
        walletTransaction.setAmount(30.0);
        walletTransaction.setTransactionId("12asd");
        walletTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        User user = new User("jay12", "Jay", false);
        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));

        Wallet wallet = new Wallet("ad23",30.0, true, user);
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/wallet/jay12/BALANCE")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        ApiError stsTransactionResp = mapper.readValue(mvcResult.getResponse().getContentAsString(), ApiError.class);
        assertEquals(HttpStatus.FORBIDDEN, stsTransactionResp.getStatus());
        assertEquals("User Details not found!", stsTransactionResp.getMessage());
    }

    @Test
    public void testAllTransactions() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        StsTransaction walletTransaction = new StsTransaction();
        walletTransaction.setTransactionType(TransactionType.CREDIT);
        walletTransaction.setUser("jay123");
        walletTransaction.setAmount(30.0);
        walletTransaction.setTransactionId("12asd");
        walletTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        User user = new User("jay12", "Jay", true);
        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));

        Transaction transaction = new Transaction("1212", TransactionType.CREDIT, 3.0, user, TransactionStatus.SUCCESS, LocalDateTime.now());
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionService.getAllTransactionByUserId(anyString())).thenReturn(transactionList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/wallet/jay12/TRANSACTIONS")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        StsTransaction stsTransactionResp = mapper.readValue(mvcResult.getResponse().getContentAsString(), StsTransaction.class);
        assertEquals(TransactionStatus.SUCCESS, stsTransactionResp.getTransactionStatus());
        assertEquals(1, stsTransactionResp.getTransactions().size());
    }
}
