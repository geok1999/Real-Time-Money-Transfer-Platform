package gr.fetrax.services;


import gr.fetrax.domain.dto.Transaction;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface TransactionService {
    Transaction createNewTransaction(Transaction transaction) throws AccountNotFoundException;

    List<Transaction> listTransactions();
}
