package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.DTO.NewClientApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
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

import static com.mindhub.homebanking.Utils.AccountUtil.generateAccountNumber;
import static com.mindhub.homebanking.Utils.ClientUtil.verifiedUserEmail;
import static com.mindhub.homebanking.Utils.ClientUtil.verifiedUserPassword;

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
    public Set<ClientDTO> getAllClientsDTO() {
        return clientService.getAllClientsDTO();
    }

    @GetMapping("/client/{id}")
    public ClientDTO getClientById(@PathVariable Long id) {
        return clientService.getClientDTOById(id);
    }

    @PostMapping("/clients")
    public ResponseEntity<String> registerUser(@RequestBody NewClientApplicationDTO newClient,
                                                Authentication currentClient) {

        if (newClient.createAdmin() && currentClient == null) {
            return new ResponseEntity<>("You are not authenticated, you cannot perform this action.", HttpStatus.FORBIDDEN);
        }

        if (currentClient != null) {
            boolean isAdmin = //GrantedAuthority
                    currentClient.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

            if (!isAdmin && newClient.createAdmin()) {
                return new ResponseEntity<>("You are not an admin, you cannot create admins.", HttpStatus.FORBIDDEN);
            }
        }


        if (newClient.email().isBlank()) {
            return new ResponseEntity<>("The email is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }

        if (clientService.existsClientByEmail(newClient.email())) {
            return new ResponseEntity<>("The e-mail address you entered is already registered.", HttpStatus.FORBIDDEN);
        }

        if (!verifiedUserEmail(newClient.email())) {
            return new ResponseEntity<>("The email must contain an '@', something after the '@'; a ' . ', and something after the ' . '\n" +
                    "Example: email@email.com", HttpStatus.FORBIDDEN);
        }

        if (newClient.firstName().isBlank()) {
            return new ResponseEntity<>("The name is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }

        if (newClient.lastName().isBlank()) {
            return new ResponseEntity<>("The last name is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }

        if (newClient.password().isBlank()) {
            return new ResponseEntity<>( "The password is not valid, try to fill in the field.", HttpStatus.FORBIDDEN);
        }

        if (!verifiedUserPassword(newClient.password())) {
            return new ResponseEntity<>( "The password must have a minimum of 8 and a maximum of 15 characters, at " +
                    "least one capital letter and one number. Special characters are not accepted.", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(newClient.firstName(), newClient.lastName(), newClient.email(),
                passwordEncoder.encode(newClient.password()), newClient.createAdmin());
        clientService.saveClient(client);

        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountService.existsAccountByNumber("VIN-" + accountNumber));

        Account account = new Account(accountNumber, LocalDate.now(), newClient.createAdmin() ? 10000.00 : 0.00,
                AccountType.SAVINGS);
        client.addAccount(account);
        accountService.saveAccount(account);

        if (newClient.createAdmin()) {
            return new ResponseEntity<>("Admin created successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
        }

    }

    @PatchMapping("clients/admin")
    public ResponseEntity<String> modifyClientToAdmin(@RequestParam String clientEmail) {

        if (!clientService.existsClientByEmail(clientEmail)) {
            return new ResponseEntity<>("The client you want to modify to admin does not exist.", HttpStatus.FORBIDDEN);
        }

        if (clientService.existsClientByEmailAndIsAdmin(clientEmail, true)) {
            return new ResponseEntity<>("The client to be modified to admin is already an admin.", HttpStatus.FORBIDDEN);
        }

        clientService.modifyClientToAdminByEmail(clientEmail);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication currentClient) {
        return new ClientDTO(clientService.getClientByEmail(currentClient.getName()));
    }
}
