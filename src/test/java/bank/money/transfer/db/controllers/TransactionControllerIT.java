package bank.money.transfer.db.controllers;

import bank.money.transfer.util.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.money.transfer.db.domain.dto.Account;
import bank.money.transfer.db.domain.dto.Transaction;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static bank.money.transfer.db.DataInputTest.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;
    @Test
    public void testTransactionCreationSuccess() throws Exception {
        Account sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("100.00"))
                .currency(Currency.USD)
                //.createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Account targetAccount = Account.builder()
                .id(2L)
                .balance(new BigDecimal("50.00"))
                .currency(Currency.USD)
               // .createdAt(LocalDateTime.parse("2024-01-15T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
        accountService.createUpdate(sourceAccount);
        accountService.createUpdate(targetAccount);

        final Transaction transaction = transactionTest(1L,2L,new BigDecimal("60.0"),Currency.USD);
        final ObjectMapper objectMapper= new ObjectMapper();
        final String transactionJSON= objectMapper.writeValueAsString(transaction);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/transactions/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(transactionJSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].sourceAccountId").value(transaction.getSourceAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].targetAccountId").value(transaction.getTargetAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].amount").value(transaction.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].currency").value(transaction.getCurrency().toString()));

    }

    @Test
    public void testTransactionFromSameSourceSimultaneouslySuccess() throws JsonProcessingException {
        Account sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("50.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Account targetAccount1 = Account.builder()
                .id(2L)
                .balance(new BigDecimal("50.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Account targetAccount2 = Account.builder()
                .id(3L)
                .balance(new BigDecimal("30.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        accountService.createUpdate(sourceAccount);
        accountService.createUpdate(targetAccount1);
        accountService.createUpdate(targetAccount2);
        final Transaction transaction1 = transactionTest(1L,2L,new BigDecimal("20.0"),Currency.USD);
        final Transaction transaction2 = transactionTest(1L,3L,new BigDecimal("20.0"),Currency.USD);


        final ObjectMapper objectMapper = new ObjectMapper();
        final String transactionJSON1 = objectMapper.writeValueAsString(transaction1);
        final String transactionJSON2 = objectMapper.writeValueAsString(transaction2);

        Runnable task1 = () -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(transactionJSON1))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].sourceAccountId").value(transaction1.getSourceAccountId()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].targetAccountId").value(transaction1.getTargetAccountId()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].amount").value(transaction1.getAmount()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].currency").value(transaction1.getCurrency().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Runnable task2 = () -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(transactionJSON2))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].sourceAccountId").value(transaction2.getSourceAccountId()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].targetAccountId").value(transaction2.getTargetAccountId()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$['Transaction: '].amount").value(transaction2.getAmount()))
                        .andExpect(MockMvcResultMatchers.jsonPath("['Transaction: '].currency").value(transaction2.getCurrency().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Account updatedSourceAccount = accountService.findById(1L).orElseThrow();
        Account updatedTargetAccount1 = accountService.findById(2L).orElseThrow();
        Account updatedTargetAccount2 = accountService.findById(3L).orElseThrow();
        System.out.println( updatedSourceAccount.getBalance());
        System.out.println( updatedTargetAccount1.getBalance());
        System.out.println( updatedTargetAccount2.getBalance());

    }


}
