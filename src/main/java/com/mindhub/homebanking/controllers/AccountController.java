package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

import static com.mindhub.homebanking.Utils.AccountUtil.generateAccountNumber;
import static com.mindhub.homebanking.Utils.AccountUtil.verifiedNumberAccountNotRepeatDB;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/accounts")
    public Set<AccountDTO> getAllAccountsDTO() {
        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountDTOById(@PathVariable Long accountId) {
        return accountService.getAccountDTOById(accountId);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount(@RequestParam String typeAccount, Authentication currentClient) {

        if (typeAccount.isBlank() || !(typeAccount.equals("SAVINGS") || typeAccount.equals("CHECKINGS"))) {
            return new ResponseEntity<>("To create an account you need to send the type of account you want to create.",
                    HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (accountService.countAccountsByClientAndIsDeleted(client, false) >= 3) {
            return new ResponseEntity<>("Cannot create any more accounts for this client", HttpStatus.FORBIDDEN);
        }

        Account account = new Account(verifiedNumberAccountNotRepeatDB(accountService), LocalDate.now(), 0.00,
                AccountType.valueOf(typeAccount));
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created!", HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/accounts/delete")
    public ResponseEntity<String> deletedAccount(@RequestParam Long accountId, Authentication currentClient) {

        if (!accountService.existsAccountById(accountId)) {
            return new ResponseEntity<>("The account you are trying to delete does not exist.", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (!accountService.existsAccountByIdAndClient(accountId, client)) {
            return new ResponseEntity<>("The account you are trying to delete does not belong to you.", HttpStatus.FORBIDDEN);
        }

        if (accountService.countAccountsByClientAndIsDeleted(client, false) == 1) {
            return new ResponseEntity<>("You only have one account, you cannot delete it.", HttpStatus.FORBIDDEN);
        }

        if (accountService.existsAccountByIdAndBalanceGreaterThan(accountId, 0.0)) {
            return new ResponseEntity<>("This account cannot be deleted, it contains money.", HttpStatus.FORBIDDEN);
        }

        accountService.deletedAccountById(accountId);

        return new ResponseEntity<>("Account deleted!", HttpStatus.OK);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountsDTOClientCurrent(Authentication currentClient) {
        return accountService.getAllAccountsDTOByClient(clientService.getClientByEmail(currentClient.getName()));
    }

}
