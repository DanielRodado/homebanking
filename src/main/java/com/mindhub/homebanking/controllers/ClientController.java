package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

import static com.mindhub.homebanking.utils.AccountUtil.generateAccountNumber;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @GetMapping("/clients")
    public Set<ClientDTO> getAllClients() {
        return clientService.getAllClientsDTO();
    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClientDTOById(id);
    }

    // Register
    @PostMapping("/clients")
    public ResponseEntity<String> newClient(
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank()) {
            return new ResponseEntity<>("The name is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }if (lastName.isBlank()) {
            return new ResponseEntity<>("The last name is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }if (email.isBlank()) {
            return new ResponseEntity<>("The email is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }if (password.isBlank()) {
            return new ResponseEntity<>( "The password is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }

        if (clientService.existsClientByEmail(email)) {
            return new ResponseEntity<>("The e-mail address you entered is already registered.", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), false);
        clientService.saveClient(client);

        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountService.existsAccountByNumber("VIN-" + accountNumber));

        Account account = new Account(accountNumber, LocalDate.now(), 0.00);
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication currentClient){
        return new ClientDTO(clientService.getClientByEmail(currentClient.getName()));
    }
}
