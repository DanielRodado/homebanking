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

    Set<Account> filterAccountsNotDeleted();

    Set<AccountDTO> getAllAccountsDTO();

    Set<Account> getAllAccountsByClientAndIsDeleteIsFalse(Client client);

    Set<AccountDTO> getAllAccountsDTOByClient(Client client);

    Account getAccountById(Long id);

    AccountDTO getAccountDTOById(Long id);

    Account getAccountByNumber(String number);

    void deletedAccountById(Long id);

    byte countAccountsByClientAndIsDeleted(Client client, Boolean isDeleted);

    byte countAccountsByIdAndClient(Long id, Client client);

    boolean existsAccountById(Long id);

    boolean existsAccountByNumber(String number);

    boolean existsAccountByClientAndNumber(Client client, String number);

    boolean existsAccountByIdAndClient(Long id, Client client);

    boolean existsAccountByIdAndBalanceGreaterThan(Long id, Double balance);

    boolean existsAccountByNumberAndBalanceLessThan(String accountNumber, Double balance);

    void saveAccount(Account account);
}
