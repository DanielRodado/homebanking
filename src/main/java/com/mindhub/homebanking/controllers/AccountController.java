package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    public String generateRandomNumber() {
        return "VIN-" + (int) ((Math.random() * (99999999)));
    }

    public String validateNumber() {
        String accountNumber = generateRandomNumber();

        while(accountRepository.existsByNumber(accountNumber)) {
            accountNumber = generateRandomNumber();
        }

        return accountNumber;
    }

    @RequestMapping("/accounts")
    public Set<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Cannot create any more accounts for this client", HttpStatus.FORBIDDEN);
        }

        Account account = new Account(validateNumber(), LocalDate.now(), 0.00);
        client.addAccount(account);

        accountRepository.save(account);
        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
