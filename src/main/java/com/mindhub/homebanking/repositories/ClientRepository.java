package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIsAdmin(String email, Boolean isAdmin);

    @Transactional
    @Modifying
    @Query("UPDATE Client c SET c.isAdmin = true WHERE c.email = :clientEmail")
    void modifyClientToAdminByEmail(@Param("clientEmail") String email);

}
