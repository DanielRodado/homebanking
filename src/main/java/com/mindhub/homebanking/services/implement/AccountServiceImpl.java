package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Set<Account> filterAccountsNotDeleted() {
        return getAllAccounts().stream().filter(account -> !account.getDeleted()).collect(Collectors.toSet());
    }

    @Override
    public Set<AccountDTO> getAllAccountsDTO() {
        return filterAccountsNotDeleted().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Set<Account> getAllAccountsByClientAndIsDeleteIsFalse(Client client) {
        return accountRepository.findByClientAndIsDeleted(client, false);
    }

    @Override
    public Set<AccountDTO> getAllAccountsDTOByClient(Client client) {
        return getAllAccountsByClientAndIsDeleteIsFalse(client)
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public AccountDTO getAccountDTOById(Long id) {
        return new AccountDTO(getAccountById(id));
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account getAccountOfClientWithGreaterBalance(Client client) {
        return accountRepository.findTopByClientOrderByBalanceDesc(client);
    }

    @Override
    public void deletedAccountById(Long id) {
        accountRepository.softDeleteAccountById(id);
    }

    @Override
    public byte countAccountsByClientAndIsDeleted(Client client, Boolean isDeleted) {
        return accountRepository.countByClientAndIsDeleted(client, isDeleted);
    }

    @Override
    public boolean existsAccountById(Long id) {
        return accountRepository.existsById(id);
    }

    @Override
    public boolean existsAccountByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    @Override
    public boolean existsAccountByClientAndNumber(Client client, String number) {
        return accountRepository.existsByClientAndNumber(client, number);
    }

    @Override
    public boolean existsAccountByIdAndClient(Long id, Client client) {
        return accountRepository.existsByIdAndClient(id, client);
    }

    @Override
    public boolean existsAccountByIdAndBalanceGreaterThan(Long id, Double balance) {
        return accountRepository.existsByIdAndBalanceGreaterThan(id, balance);
    }

    @Override
    public boolean existsAccountByNumberAndBalanceLessThan(String accountNumber, Double balance) {
        return accountRepository.existsByNumberAndBalanceLessThan(accountNumber, balance);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}