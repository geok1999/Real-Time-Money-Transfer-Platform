package gr.fetrax.services.implementation;


import gr.fetrax.domain.dto.Account;
import gr.fetrax.domain.dto.Transaction;
import gr.fetrax.domain.entities.TransactionEntity;
import gr.fetrax.repositories.TransactionRepository;
import gr.fetrax.services.AccountService;
import gr.fetrax.services.TransactionService;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplementation implements TransactionService {
    private  final TransactionRepository transactionRepository;
    private final AccountService accountService;


    @Autowired
    public TransactionServiceImplementation(final TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }


    @Override
    @Retryable(
            retryFor = {CannotAcquireLockException.class, OptimisticLockingFailureException.class, OptimisticLockException.class},
            backoff = @Backoff(delay = 1000)
    )
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Transaction createNewTransaction(Transaction transaction) throws AccountNotFoundException {

        Account accountSource = accountService.findById(transaction.getSourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        Account accountTarget = accountService.findById(transaction.getTargetAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Target account not found"));


        /*
            Business Requirements
        */


        //First case:Transfer between the same account
        if (Objects.equals(accountSource.getId(), accountTarget.getId())) {
            throw new IllegalArgumentException("Source and Target account can't be the same!");
        }
        //Second case: Insufficient balance
        if (accountSource.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new IllegalArgumentException("Source has Insufficient balance!");
        }
        //Third case: Currency mismatch
        if (!accountSource.getCurrency().equals(accountTarget.getCurrency()) || !accountSource.getCurrency().equals(transaction.getCurrency())) {
            if (!accountSource.getCurrency().equals(accountTarget.getCurrency()))
                throw new IllegalArgumentException("Currency Source and Currency Target must be the same!");
            else
                throw new IllegalArgumentException("Currency Source and Currency Target must match with the Transaction Currency!");
        }


        accountSource.setBalance(accountSource.getBalance().subtract(transaction.getAmount()));
        accountTarget.setBalance(accountTarget.getBalance().add(transaction.getAmount()));

        accountService.createUpdate(accountSource);
        accountService.createUpdate(accountTarget);

        final TransactionEntity transactionEntity = transactionToTransactionEntity(transaction);
        final TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        return transactionEntityToTransaction(savedTransaction);


          //  throw new IllegalArgumentException("Transaction failed due to a concurrent modification. Please try again later.", e);
    }

    @Override
    public List<Transaction> listTransactions() {
        final List<TransactionEntity> transactionEntitiesList = transactionRepository.findAll();
        return transactionEntitiesList.stream().map(this::transactionEntityToTransaction).collect(Collectors.toList());
    }

    /*
      This is necessary because we don't want to leak our abstraction of a Transaction entity, so we use Transaction but is
      necessary to convert to a Transaction entity
   */
    private TransactionEntity transactionToTransactionEntity(Transaction transaction){
        return TransactionEntity.builder()
                .id(transaction.getId())
                .targetAccountId(transaction.getTargetAccountId())
                .sourceAccountId(transaction.getSourceAccountId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .build();
    }

    private Transaction transactionEntityToTransaction(TransactionEntity transactionEntity){
        return Transaction.builder()
                .id(transactionEntity.getId())
                .targetAccountId(transactionEntity.getTargetAccountId())
                .sourceAccountId(transactionEntity.getSourceAccountId())
                .amount(transactionEntity.getAmount())
                .currency(transactionEntity.getCurrency())
                .createdAt(transactionEntity.getCreatedAt())
                .build();

    }

}
