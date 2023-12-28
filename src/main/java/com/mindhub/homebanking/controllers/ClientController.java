package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientServices clientServices;

    @GetMapping("/clients")
    public Set<ClientDTO> getAllClientsDTO() {
        return clientServices.getAllClientsDTO();
    }
}
