package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String firstName, lastName, email;
    private Boolean isAdmin;

    private Set<AccountDTO> accounts;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getName();
        this.lastName = client.getLastName();
        this.isAdmin = client.getAdmin();
        this.accounts = client.getAccounts()
                .stream()
                .filter(account -> !account.getDeleted())
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
}
