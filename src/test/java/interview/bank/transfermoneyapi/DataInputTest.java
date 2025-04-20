package interview.bank.transfermoneyapi;

import interview.bank.transfermoneyapi.domain.dto.Account;
import interview.bank.transfermoneyapi.domain.dto.Transaction;
import interview.bank.transfermoneyapi.domain.entities.AccountEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DataInputTest {
    private DataInputTest() {
    }

    public static Account accountTest(){
        return Account.builder()
                .id(1L)
                .balance(new BigDecimal("2345.67"))
                .currency("USD")
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public static AccountEntity accountEntityTest(){
        return AccountEntity.builder()
                .id(1L)
                .balance(new BigDecimal("2345.67"))
                .currency("USD")
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public static Transaction transactionTest(Long sourceAccountId, Long targetAccountId, BigDecimal amount, String currency) {
        return Transaction.builder()
                .sourceAccountId(sourceAccountId)
                .targetAccountId(targetAccountId)
                .amount(amount)
                .currency(currency)
                .build();
    }
}
