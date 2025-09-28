package bank.money.transfer.services;


import bank.money.transfer.domain.dto.Transaction;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface TransactionService {
    Transaction createNewTransaction(Transaction transaction) throws AccountNotFoundException;

    List<Transaction> listTransactions();
}
