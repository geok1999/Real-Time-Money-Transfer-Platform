package bank.money.transfer.db.controllers;


import bank.money.transfer.db.domain.dto.Transaction;
import bank.money.transfer.db.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    //Execute a transaction
    @PostMapping(path = "/transaction")
    public ResponseEntity<?> createTransaction(@RequestBody final Transaction transaction){
        try{
            final Transaction newTransaction=transactionService.createNewTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Message: ","Transaction was completed successfully","Transaction: ",newTransaction));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("Error: ",e.getMessage()));
        }
    }
    //List all transactions
    @GetMapping(path = "/LogTransaction")
    public ResponseEntity<List<Transaction>> ListAllTransactions(){
        return new ResponseEntity<>(transactionService.listTransactions(),HttpStatus.OK);
    }

}
