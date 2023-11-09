package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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

        if (accountService.countAccountsByClient(client) >= 3) {
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

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountClientCurrent(Authentication currentClient) {
        return accountService.getAllAccountsDTOByClient(clientService.getClientByEmail(currentClient.getName()));
    }
}
