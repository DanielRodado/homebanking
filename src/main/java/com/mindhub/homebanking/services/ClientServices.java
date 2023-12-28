package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.Set;

public interface ClientServices {

    Set<Client> getAllClients();

    Set<ClientDTO> getAllClientsDTO();

}
