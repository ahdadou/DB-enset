package com.DigitalBanking.controller;

import com.DigitalBanking.config.TestSecurityConfig;
import com.DigitalBanking.dtos.CustomerDTO;
import com.DigitalBanking.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(TestSecurityConfig.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    private CustomerDTO customerDTO;

    @BeforeEach
    public void setUp() {
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john.doe@example.com");
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        List<CustomerDTO> customerDTOList = Arrays.asList(customerDTO);
        when(bankAccountService.listCustomers()).thenReturn(customerDTOList);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    public void testSearchCustomers() throws Exception {
        List<CustomerDTO> customerDTOList = Arrays.asList(customerDTO);
        when(bankAccountService.searchCustomers(Mockito.anyString())).thenReturn(customerDTOList);

        mockMvc.perform(get("/api/customers/search?keyword=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        when(bankAccountService.getCustomer(anyLong())).thenReturn(customerDTO);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        when(bankAccountService.saveCustomer(Mockito.any(CustomerDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        when(bankAccountService.updateCustomer(Mockito.any(CustomerDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isOk());
    }
}
