package interview.bank.transfermoneyapi.services.implementation;

import interview.bank.transfermoneyapi.domain.dto.Account;
import interview.bank.transfermoneyapi.domain.entities.AccountEntity;
import interview.bank.transfermoneyapi.repositories.AccountRepository;
import interview.bank.transfermoneyapi.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplementation implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImplementation(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createUpdate(final Account account) {
        final AccountEntity accountEntity = accountToAccountEntity(account);
        final AccountEntity savedAccountEntity= accountRepository.save(accountEntity);
        return accountEntityToAccount(savedAccountEntity);
    }

    @Override
    public boolean isAccountExists(Account account) {
        return accountRepository.existsById(account.getId());
    }

    @Override
    public Optional<Account> findById(Long id) {
        final Optional<AccountEntity> accountFoundedById= accountRepository.findById(id);
        return accountFoundedById.map(this::accountEntityToAccount);
    }

    @Override
    public List<Account> listAccounts() {
        final List<AccountEntity> accountEntitiesList = accountRepository.findAll();
        return accountEntitiesList.stream().map(this::accountEntityToAccount).collect(Collectors.toList());

    }
    /*
        This is necessary because we don't want to leak our abstraction of an account entity, so we use account but is
        necessary to convert to an account entity
     */
    private AccountEntity accountToAccountEntity(Account account){
        return AccountEntity.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .build();
    }

    private Account accountEntityToAccount(AccountEntity accountEntity){
        return Account.builder()
                .id(accountEntity.getId())
                .balance(accountEntity.getBalance())
                .currency(accountEntity.getCurrency())
                .createdAt(accountEntity.getCreatedAt())
                .build();
    }

}
