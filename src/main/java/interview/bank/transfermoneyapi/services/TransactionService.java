package interview.bank.transfermoneyapi.services;


import interview.bank.transfermoneyapi.domain.dto.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createNewTransaction(Transaction transaction);

    List<Transaction> listTransactions();
}
