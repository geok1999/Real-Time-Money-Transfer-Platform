package bank.money.transfer.db.controllers;

import bank.money.transfer.util.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import bank.money.transfer.db.domain.dto.Account;
import bank.money.transfer.db.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static bank.money.transfer.db.DataInputTest.accountTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class AccountControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Test
    public void testAccountCreation() throws Exception {
        final Account account = accountTest();
        final ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        final String accountJSON= objectMapper.writeValueAsString(account);

        mockMvc.perform(post("/api/accounts/account/" + account.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(accountJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.balance").value(account.getBalance()))
                .andExpect(jsonPath("$.currency").value(account.getCurrency().name()));
                //.andExpect(jsonPath("$.createdAt").value(account.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    public void testAccountUpdate() throws Exception {
        final Account account = accountTest();
        accountService.createUpdate(account);
        account.setCurrency(Currency.TRY);
        final ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        final String accountJSON= objectMapper.writeValueAsString(account);

        mockMvc.perform(post("/api/accounts/account/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.balance").value(account.getBalance()))
                .andExpect(jsonPath("$.currency").value(account.getCurrency().name()));
               // .andExpect(jsonPath("$.createdAt").value(account.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }


    @Test
    public void testAccountFindingCaseNotExists() throws Exception {
        mockMvc.perform(get("/accounts/"+5L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAccountFindingCaseExists() throws Exception {
        final Account account = accountTest();

        accountService.createUpdate(account);

        mockMvc.perform(get("/api/accounts/account/"+account.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.balance").value(account.getBalance()))
                .andExpect(jsonPath("$.currency").value(account.getCurrency().name()));
                //.andExpect(jsonPath("$.createdAt").value(account.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    public void testListOfAllAccountsForEmptyList() throws Exception {
        mockMvc.perform(get("/api/accounts/account"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }
    @Test
    public void testListOfAllAccountsForNonEmptyList() throws Exception {
        final Account account = accountTest();
        accountService.createUpdate(account);

        mockMvc.perform(get("/api/accounts/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(account.getId()))
                .andExpect(jsonPath("$.[0].balance").value(account.getBalance()))
                .andExpect(jsonPath("$.[0].currency").value(account.getCurrency().name()));
               // .andExpect(jsonPath("$.[0].createdAt").value(account.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

    }


}
