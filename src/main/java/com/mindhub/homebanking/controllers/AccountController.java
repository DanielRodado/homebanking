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

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    public int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String generateAccountNumber() {
        int quantityOfNumbers = generateRandomNumber(3, 8);
        StringBuilder accountNumber;
        do {
            accountNumber = new StringBuilder();
            for (byte i = 0; i <= quantityOfNumbers; i++) {
                accountNumber.append(generateRandomNumber(0, 9));
            }
        } while (accountService.existsAccountByNumber("VIN-" + accountNumber));

        return "VIN-" + accountNumber;
    }

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

        Account account = new Account(generateAccountNumber(), LocalDate.now(), 0.00);
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created!", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountClientCurrent(Authentication currentClient) {
        return accountService.getAllAccountsDTOByClient(clientService.getClientByEmail(currentClient.getName()));
    }
}
