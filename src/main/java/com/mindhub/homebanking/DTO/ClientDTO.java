package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    // Properties

    private Long id;

    private String firstName, lastName, email;

    private Boolean isAdmin;

    private Set<AccountDTO> accounts;

    private Set<ClientLoanDTO> loans;

    private Set<CardDTO> cards;

    // Method Constructor

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.isAdmin = client.getAdmin();
        this.accounts = client.getAccounts()
                .stream()
                .filter(account -> !account.getDeleted())
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
        this.loans =
                client.getClientLoans()
                        .stream()
                        .filter(clientLoan -> !clientLoan.getDeleted())
                        .map(ClientLoanDTO::new).collect(Collectors.toSet());
        this.cards = client.getCards()
                .stream()
                .filter(card -> !card.getDeleted())
                .map(CardDTO::new).collect(Collectors.toSet());
    }

    // Accessory methods

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

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
