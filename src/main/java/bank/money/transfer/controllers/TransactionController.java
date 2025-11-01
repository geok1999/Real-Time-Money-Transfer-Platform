package bank.money.transfer.controllers;


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
//TODO: fix the urls based on REST
@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    //Execute a transaction
    @PostMapping(path = "/transaction")
    public ResponseEntity<?> createTransaction(@RequestBody @Valid final Transaction transaction){
        try{
            final Transaction newTransaction=transactionService.createNewTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Message: ","Transaction was completed successfully","Transaction: ",newTransaction));
        }catch (IllegalArgumentException | AccountNotFoundException e){
            return ResponseEntity.badRequest().body(Map.of("Error: ",e.getMessage()));
        }
    }
    //List all transactions
    @GetMapping(path = "/LogTransaction")
    public ResponseEntity<List<Transaction>> listAllTransactions(){
        return new ResponseEntity<>(transactionService.listTransactions(),HttpStatus.OK);
    }

}
