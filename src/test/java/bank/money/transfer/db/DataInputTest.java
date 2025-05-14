package bank.money.transfer.db;

import bank.money.transfer.db.domain.dto.Account;
import bank.money.transfer.db.domain.dto.Transaction;
import bank.money.transfer.db.domain.entities.AccountEntity;
import bank.money.transfer.util.Currency;

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
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public static AccountEntity accountEntityTest(){
        return AccountEntity.builder()
                .id(1L)
                .balance(new BigDecimal("2345.67"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public static Transaction transactionTest(Long sourceAccountId, Long targetAccountId, BigDecimal amount, Currency currency) {
        return Transaction.builder()
                .sourceAccountId(sourceAccountId)
                .targetAccountId(targetAccountId)
                .amount(amount)
                .currency(currency)
                .build();
    }
}
