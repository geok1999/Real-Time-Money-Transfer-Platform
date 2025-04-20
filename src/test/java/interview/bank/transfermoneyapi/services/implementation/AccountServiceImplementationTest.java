package interview.bank.transfermoneyapi.services.implementation;

import interview.bank.transfermoneyapi.domain.dto.Account;
import interview.bank.transfermoneyapi.domain.entities.AccountEntity;
import interview.bank.transfermoneyapi.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static interview.bank.transfermoneyapi.DataInputTest.accountEntityTest;
import static interview.bank.transfermoneyapi.DataInputTest.accountTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AccountServiceImplementationTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImplementation accountServiceImplementationTest;

    @Test
    public void testAccountIfSaved(){
        final Account account= accountTest();
        final AccountEntity accountEntity= accountEntityTest();

        when(accountRepository.save(eq(accountEntity))).thenReturn(accountEntity);
        final Account resultAccount = accountServiceImplementationTest.createUpdate(account);

        assertEquals(account,resultAccount);

    }
    @Test
    public void testAccountNotFoundedById(){
        Long id=5L;
        when(accountRepository.findById(eq(id))).thenReturn(Optional.empty());
        final Optional<Account> account=accountServiceImplementationTest.findById(id);
        assertEquals(Optional.empty(),account);
    }

    @Test
    public void testAccountFoundedById(){
        final Account account= accountTest();
        final AccountEntity accountEntity= accountEntityTest();


        when(accountRepository.findById(eq(account.getId()))).thenReturn(Optional.of(accountEntity));
        final Optional<Account> accountResult=accountServiceImplementationTest.findById(accountEntity.getId());
        assertEquals(Optional.of(account),accountResult);
    }

    @Test
    public void testListOfAccountForEmptyList(){
        when(accountRepository.findAll()).thenReturn(new ArrayList<>());
        final List<Account> accountResultList = accountServiceImplementationTest.listAccounts();
        assertEquals(0,accountResultList.size());
    }

    @Test
    public void testListOfAccountForNonEmptyList(){
        final AccountEntity accountEntity= accountEntityTest();
        when(accountRepository.findAll()).thenReturn(List.of(accountEntity));
        final List<Account> accountResultList = accountServiceImplementationTest.listAccounts();
        assertEquals(1,accountResultList.size());
    }

    @Test
    public void testIfAccountExists(){

        when(accountRepository.existsById(any())).thenReturn(true);
        final boolean accountResultBool = accountServiceImplementationTest.isAccountExists(accountTest());

        assertTrue(accountResultBool);

    }

}
