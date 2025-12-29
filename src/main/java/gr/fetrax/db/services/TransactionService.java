package gr.fetrax.db.services;


import gr.fetrax.db.domain.dto.Transaction;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface TransactionService {
    Transaction createNewTransaction(Transaction transaction) throws AccountNotFoundException;

    List<Transaction> listTransactions();
}
