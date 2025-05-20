package bank.money.transfer.db.services.implementation;


import bank.money.transfer.db.domain.dto.Account;
import bank.money.transfer.db.domain.dto.Transaction;
import bank.money.transfer.db.domain.entities.TransactionEntity;
import bank.money.transfer.db.repositories.TransactionRepository;
import bank.money.transfer.db.services.AccountService;
import bank.money.transfer.util.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static bank.money.transfer.db.DataInputTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplementationTest {
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionServiceImplementation transactionServiceImplementation;
    @Mock
    private AccountService  accountService;

    @Test
    public void testCreateNewTransaction(){
        Account sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Account targetAccount = Account.builder()
                .id(2L)
                .balance(new BigDecimal("50.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();


        final Transaction transaction =transactionTest(1L,2L,new BigDecimal("60.0"),Currency.USD);

        when(accountService.findById(eq(sourceAccount.getId()))).thenReturn(Optional.of(sourceAccount));
        when(accountService.findById(eq(targetAccount.getId()))).thenReturn(Optional.of(targetAccount));
        when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(i -> i.getArgument(0));

        final Transaction resultTransaction = transactionServiceImplementation.createNewTransaction(transaction);
        assertNotNull(resultTransaction);
        assertEquals(new BigDecimal("40.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("110.00"), targetAccount.getBalance());
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
        verify(accountService, times(2)).createUpdate(any(Account.class));

    }

    @Test
    public void testCreateNewTransactionAccountNotExist(){
        Account sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        final Transaction transaction =transactionTest(1L,2L,new BigDecimal("60.0"),Currency.USD);
        when(accountService.findById(eq(sourceAccount.getId()))).thenReturn(Optional.of(sourceAccount));
        when(accountService.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class,()->transactionServiceImplementation.createNewTransaction(transaction));

        assertEquals("Source or Target account not Exist!", argumentException.getMessage());
    }

    @Test
    public void testCreateNewTransactionTransferBetweenTheSameAccount(){
        Account sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        final Transaction transaction =transactionTest(1L,1L,new BigDecimal("20.0"),Currency.USD);
        when(accountService.findById(eq(sourceAccount.getId()))).thenReturn(Optional.of(sourceAccount));

        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class,()->transactionServiceImplementation.createNewTransaction(transaction));

        assertEquals("Source and Target account can't be the same!", argumentException.getMessage());
    }

    @Test
    public void testCreateNewTransactionInsufficientBalance(){
        Account sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("10.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Account targetAccount = Account.builder()
                .id(2L)
                .balance(new BigDecimal("50.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();


        final Transaction transaction =transactionTest(1L,2L,new BigDecimal("60.0"),Currency.USD);
        when(accountService.findById(eq(sourceAccount.getId()))).thenReturn(Optional.of(sourceAccount));
        when(accountService.findById(eq(targetAccount.getId()))).thenReturn(Optional.of(targetAccount));

        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class,()->transactionServiceImplementation.createNewTransaction(transaction));

        assertEquals("Source has Insufficient balance!", argumentException.getMessage());
    }

    @Test
    public void testCreateNewTransactionCurrencyMismatchCase1(){
        Account sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Account targetAccount = Account.builder()
                .id(2L)
                .balance(new BigDecimal("50.00"))
                .currency(Currency.TRY)
                .createdAt(LocalDateTime.parse("2024-01-15T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();


        final Transaction transaction =transactionTest(1L,2L,new BigDecimal("60.0"),Currency.USD);
        when(accountService.findById(eq(sourceAccount.getId()))).thenReturn(Optional.of(sourceAccount));
        when(accountService.findById(eq(targetAccount.getId()))).thenReturn(Optional.of(targetAccount));

        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class,()->transactionServiceImplementation.createNewTransaction(transaction));

        assertEquals("Currency Source and Currency Target must be the same!", argumentException.getMessage());

    }
    @Test
    public void testCreateNewTransactionCurrencyMismatchCase2() {
        Account sourceAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Account targetAccount = Account.builder()
                .id(2L)
                .balance(new BigDecimal("50.00"))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.parse("2024-01-15T13:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();


        final Transaction transaction = transactionTest(1L,2L,new BigDecimal("20.0"),Currency.EUR);
        when(accountService.findById(eq(sourceAccount.getId()))).thenReturn(Optional.of(sourceAccount));
        when(accountService.findById(eq(targetAccount.getId()))).thenReturn(Optional.of(targetAccount));

        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class, () -> transactionServiceImplementation.createNewTransaction(transaction));

        assertEquals("Currency Source and Currency Target must match with the Transaction Currency!", argumentException.getMessage());

    }

}
