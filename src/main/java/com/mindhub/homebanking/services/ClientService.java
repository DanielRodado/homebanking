package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.Set;

public interface ClientService {

    Set<Client> getAllClients();

    Set<ClientDTO> getAllClientsDTO();

    Client getClientById(Long id);

    ClientDTO getClientDTOById(Long id);

    Client getClientByEmail(String email);

    boolean existsClientByEmail(String email);

    boolean existsClientByEmailAndIsAdmin(String email, Boolean isAdmin);

    void modifyClientToAdminByEmail(String email);

    void saveClient(Client client);

}
