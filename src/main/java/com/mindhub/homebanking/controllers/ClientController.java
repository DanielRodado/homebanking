package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
