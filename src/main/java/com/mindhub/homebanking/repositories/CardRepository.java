package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);

    boolean existsByCvv(String cvv);

    boolean existsByNumberAndCvv(String number, String cvv);

    boolean existsByIdAndClient(Long id, Client client);

    boolean existsByClientAndTypeAndColorAndIsDeleted(Client client, CardType type, CardColor color, Boolean isDeleted);

    boolean existsByNumberAndThruDateBefore(String number, LocalDate thruDate);

    @Transactional
    @Modifying
    @Query("UPDATE Card c SET c.isDeleted = true WHERE c.id = :cardId")
    void softDeleteCardById(@Param("cardId") Long id);

    @Transactional
    @Query("SELECT c.client FROM Card c WHERE c.number = :numberCard")
    Client getClientByNumberCard(@Param("numberCard") String number);

}