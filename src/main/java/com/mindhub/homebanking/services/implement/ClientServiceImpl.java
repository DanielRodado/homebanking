package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

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

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getClientDTOById(Long id) {
        return new ClientDTO(getClientById(id));
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public boolean existsClientByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public boolean existsClientByEmailAndIsAdmin(String email, Boolean isAdmin) {
        return clientRepository.existsByEmailAndIsAdmin(email, isAdmin);
    }

    @Override
    public void modifyClientToAdminByEmail(String email) {
        clientRepository.modifyClientToAdminByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
