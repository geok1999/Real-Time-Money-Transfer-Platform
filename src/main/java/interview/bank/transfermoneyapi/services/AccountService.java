package interview.bank.transfermoneyapi.services;

import interview.bank.transfermoneyapi.domain.dto.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account createUpdate(Account account);

    boolean isAccountExists(Account account);

    Optional<Account> findById(Long id);

    List<Account> listAccounts();

}
