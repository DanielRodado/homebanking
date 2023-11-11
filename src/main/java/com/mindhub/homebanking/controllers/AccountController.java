package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Set;

import static com.mindhub.homebanking.utils.AccountUtil.generateAccountNumber;

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
    public Set<AccountDTO> getAllAccounts() {
        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccountDTOById(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication currentClient) {

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (accountService.countAccountsByClientAndIsDeleted(client, false) >= 3) {
            return new ResponseEntity<>("Cannot create any more accounts for this client", HttpStatus.FORBIDDEN);
        }

        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountService.existsAccountByNumber("VIN-" + accountNumber));

        Account account = new Account(accountNumber, LocalDate.now(), 0.00);
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created!", HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/accounts/deleted")
    public ResponseEntity<String> deletedAccount(@RequestParam Long id) {

        if (!accountService.existsAccountById(id)) {
            return new ResponseEntity<>("This account does not exist.", HttpStatus.FORBIDDEN);
        }

        accountService.deletedAccountById(id);
        transactionService.deletedTransactions(accountService.getAccountById(id));

        return new ResponseEntity<>("Account deleted!", HttpStatus.OK);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountClientCurrent(Authentication currentClient) {
        return accountService.getAllAccountsDTOByClient(clientService.getClientByEmail(currentClient.getName()));
    }
}
