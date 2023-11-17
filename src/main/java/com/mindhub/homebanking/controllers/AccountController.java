package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
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
    public ResponseEntity<Object> newAccount(@RequestParam String typeAccount, Authentication currentClient) {

        if (typeAccount.isBlank() || !(typeAccount.equals("SAVINGS") || typeAccount.equals("CHECKINGS"))) {
            return new ResponseEntity<>("To create an account you need to send the type of account you want to create.",
                    HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (accountService.countAccountsByClientAndIsDeleted(client, false) >= 3) {
            return new ResponseEntity<>("Cannot create any more accounts for this client", HttpStatus.FORBIDDEN);
        }

        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountService.existsAccountByNumber("VIN-" + accountNumber));

        Account account = new Account(accountNumber, LocalDate.now(), 0.00, AccountType.valueOf(typeAccount));
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created!", HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/accounts/delete")
    public ResponseEntity<String> deletedAccount(@RequestParam Long AccountId, Authentication currentClient) {

        if (!accountService.existsAccountById(AccountId)) {
            return new ResponseEntity<>("The account you are trying to delete does not exist.", HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsAccountByIdAndClient(AccountId, clientService.getClientByEmail(currentClient.getName()))) {
            return new ResponseEntity<>("The account you are trying to delete does not belong to you.", HttpStatus.FORBIDDEN);
        }

        if (accountService.existsAccountByIdAndBalanceGreaterThan(AccountId, 0.0)) {
            return new ResponseEntity<>("This account cannot be deleted, it contains money.", HttpStatus.FORBIDDEN);
        }

        accountService.deletedAccountById(AccountId);

        return new ResponseEntity<>("Account deleted!", HttpStatus.OK);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountClientCurrent(Authentication currentClient) {
        return accountService.getAllAccountsDTOByClient(clientService.getClientByEmail(currentClient.getName()));
    }
}
