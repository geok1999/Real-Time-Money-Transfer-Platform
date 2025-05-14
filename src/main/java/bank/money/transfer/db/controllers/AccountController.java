package bank.money.transfer.db.controllers;

import bank.money.transfer.db.domain.dto.Account;
import bank.money.transfer.db.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    //For Creating or Updating an existing Account
    @PostMapping(path = "/account/{id}")
    public ResponseEntity<Account> createOrUpdateAccount(@PathVariable final Long id, @RequestBody final Account account){
        account.setId(id);
        boolean isAccountExists =accountService.isAccountExists(account);
        final Account savedAccount=accountService.createUpdate(account);

        if(isAccountExists){
            return new ResponseEntity<>(savedAccount, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        }


    }

    //To track an Account with spesific ID
    @GetMapping(path = "/account/{id}")
    public ResponseEntity<Account> findAccount(@PathVariable final Long id){
        final Optional<Account> foundAccount=accountService.findById(id);
        return foundAccount.map(account -> new ResponseEntity<>(account,HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //To track all current Account in Database
    @GetMapping(path = "/account")
    public ResponseEntity<List<Account>> ListAllAccounts(){
        return new ResponseEntity<>(accountService.listAccounts(),HttpStatus.OK);
    }

}
