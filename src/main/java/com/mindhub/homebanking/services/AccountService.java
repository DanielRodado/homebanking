package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface AccountService {

    List<Account> getAllAccounts();

    Set<AccountDTO> getAllAccountsDTO();

    Set<Account> getAllAccountsByClient(Client client);

    Set<AccountDTO> getAllAccountsDTOByClient(Client client);

    Account getAccountById(Long id);

    AccountDTO getAccountDTOById(Long id);

    Account getAccountByNumber(String number);

    byte countAccountsByClient(Client client);

    boolean existsAccountByNumber(String number);

    boolean existsAccountByClientAndNumber(Client client, String number);

    void saveAccount(Account account);
}
