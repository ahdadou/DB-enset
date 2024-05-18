package com.DigitalBanking.controller;

import com.DigitalBanking.dtos.*;
import com.DigitalBanking.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    private BankAccountDTO bankAccountDTO;
    private AccountOperationDTO accountOperationDTO;
    private DebitDTO debitDTO;
    private CreditDTO creditDTO;
    private TransferRequestDTO transferRequestDTO;

    @BeforeEach
    public void setUp() {
        bankAccountDTO = new BankAccountDTO();
        accountOperationDTO = new AccountOperationDTO();
        accountOperationDTO.setId(1L);
        debitDTO = new DebitDTO();
        debitDTO.setAccountId("12345");
        debitDTO.setAmount(200);
        debitDTO.setDescription("Debit Test");
        creditDTO = new CreditDTO();
        creditDTO.setAccountId("12345");
        creditDTO.setAmount(200);
        creditDTO.setDescription("Credit Test");
        transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.setAccountSource("12345");
        transferRequestDTO.setAccountDestination("67890");
        transferRequestDTO.setAmount(300);
    }

    @Test
    public void testGetBankAccount() throws Exception {
        when(bankAccountService.getBankAccount(anyString())).thenReturn(bankAccountDTO);

        mockMvc.perform(get("/api/accounts/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("12345")))
                .andExpect(jsonPath("$.balance", is(1000.0)));
    }

    @Test
    public void testListAccounts() throws Exception {
        List<BankAccountDTO> bankAccountDTOList = Arrays.asList(bankAccountDTO);
        when(bankAccountService.bankAccountList()).thenReturn(bankAccountDTOList);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("12345")));
    }

    @Test
    public void testDebit() throws Exception {
        mockMvc.perform(post("/api/accounts/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\":\"12345\",\"amount\":200,\"description\":\"Debit Test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId", is("12345")))
                .andExpect(jsonPath("$.amount", is(200.0)));
    }

    @Test
    public void testCredit() throws Exception {
        mockMvc.perform(post("/api/accounts/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\":\"12345\",\"amount\":200,\"description\":\"Credit Test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId", is("12345")))
                .andExpect(jsonPath("$.amount", is(200.0)));
    }

    @Test
    public void testTransfer() throws Exception {
        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountSource\":\"12345\",\"accountDestination\":\"67890\",\"amount\":300}"))
                .andExpect(status().isOk());
    }
}
