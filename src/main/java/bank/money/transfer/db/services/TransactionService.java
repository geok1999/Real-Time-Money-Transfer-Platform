package bank.money.transfer.db.services;


import bank.money.transfer.db.domain.dto.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createNewTransaction(Transaction transaction);

    List<Transaction> listTransactions();
}
