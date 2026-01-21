package gr.fetrax.controllers.impl;

import gr.fetrax.controllers.AccountController;
import gr.fetrax.domain.dto.Account;
import gr.fetrax.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountControllerImpl(final AccountService accountService) {
        this.accountService = accountService;
    }

   @Override
    public ResponseEntity<Account> createOrUpdateAccount(@PathVariable final Long id, @Valid @RequestBody final Account account){
        account.setId(id);
        boolean isAccountExists =accountService.isAccountExists(account);
        final Account savedAccount=accountService.createUpdate(account);

        if(isAccountExists){
            return new ResponseEntity<>(savedAccount, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        }


    }
    @Override
    public ResponseEntity<Account> findAccount(@PathVariable final Long id){
        final Optional<Account> foundAccount=accountService.findById(id);
        return foundAccount.map(account -> new ResponseEntity<>(account,HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<List<Account>> ListAllAccounts(){
        return new ResponseEntity<>(accountService.listAccounts(),HttpStatus.OK);
    }

}
