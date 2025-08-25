package bank.money.transfer.db.services;

import bank.money.transfer.db.domain.dto.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account createUpdate(Account account);

    boolean isAccountExists(Account account);

    Optional<Account> findById(Long id);

    List<Account> listAccounts();

}
