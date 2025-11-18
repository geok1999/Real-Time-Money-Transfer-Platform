package bank.money.transfer.controllers.impl;


import bank.money.transfer.controllers.TransactionController;
import bank.money.transfer.domain.dto.Transaction;
import bank.money.transfer.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionControllerImpl(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<?> createTransaction(@RequestBody @Valid final Transaction transaction){
        try{
            final Transaction newTransaction=transactionService.createNewTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Message: ","Transaction was completed successfully","Transaction: ",newTransaction));
        }catch (IllegalArgumentException | AccountNotFoundException e){
            return ResponseEntity.badRequest().body(Map.of("Error: ",e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<List<Transaction>> listAllTransactions(){
        return new ResponseEntity<>(transactionService.listTransactions(),HttpStatus.OK);
    }

}
