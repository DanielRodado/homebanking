package com.mindhub.homebanking.services.implemet;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientServices {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Set<Client> getAllClients() {
        return new HashSet<>(clientRepository.findAll());
    }

    @Override
    public Set<ClientDTO> getAllClientsDTO() {
        return getAllClients().stream().map(ClientDTO::new).collect(Collectors.toSet());
    }
}
