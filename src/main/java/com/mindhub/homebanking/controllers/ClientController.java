package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    public int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String generateAccountNumber() {
        int quantityOfNumbers = generateRandomNumber(1, 8);
        StringBuilder accountNumber = new StringBuilder();
        do {
            for (byte i = 0; i <= quantityOfNumbers; i++) {
                accountNumber.append(generateRandomNumber(0, 9));
            }
        } while (accountRepository.existsByNumber("VIN-" + accountNumber));

        return "VIN-" + accountNumber;
    }

    @RequestMapping("/clients")
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    // Register
    @PostMapping("/clients")
    public ResponseEntity<Object> newClient(
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() ||  firstName.isBlank()) {
            return new ResponseEntity<>("The name is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }if (lastName.isEmpty() ||  lastName.isBlank()) {
            return new ResponseEntity<>("The last name is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }if (email.isEmpty() ||  email.isBlank()) {
            return new ResponseEntity<>("The email is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }if (password.isEmpty() ||  password.isBlank()) {
            return new ResponseEntity<>( "The password is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("The e-mail address you entered is already registered.", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), false);
        clientRepository.save(client);

        Account account = new Account(generateAccountNumber(), LocalDate.now(), 0.00);
        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
    }

    @RequestMapping("/clients/currents")
    public ClientDTO getClientCurrent(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}
