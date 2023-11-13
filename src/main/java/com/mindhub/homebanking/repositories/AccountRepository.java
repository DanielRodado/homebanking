package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNumber(String number);
    Account findByNumber(String number);
    byte countByClientAndIsDeleted(Client client, Boolean isDeleted);
    Set<Account> findByClientAndIsDeletedEquals(Client client, Boolean isDeleted);
    boolean existsByClientAndNumber(Client client, String number);
    boolean existsByIdAndBalanceGreaterThanEqual(Long id, Double balance);
    boolean existsByIdAndClient(Long id, Client client);
}
