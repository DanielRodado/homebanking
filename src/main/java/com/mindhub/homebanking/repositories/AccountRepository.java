package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNumber(String number);

    Account findByNumber(String number);

    byte countByClientAndIsDeleted(Client client, Boolean isDeleted);

    Set<Account> findByClientAndIsDeleted(Client client, Boolean isDeleted);

    boolean existsByClientAndNumber(Client client, String number);

    boolean existsByIdAndBalanceGreaterThan(Long id, Double balance);

    boolean existsByIdAndClient(Long id, Client client);

    boolean existsByNumberAndBalanceLessThan(String accountNumber, Double balance);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.isDeleted = true WHERE a.id = :accountId")
    void softDeleteAccountById(@Param("accountId") Long id);

    Account findTopByClientOrderByBalanceDesc(Client client);

}
