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

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);
    boolean existsByIdAndClient(Long id, Client client);
    boolean existsByClientAndTypeAndColorAndIsDeleted(Client client, CardType type, CardColor color, Boolean isDeleted);

    @Transactional
    @Modifying
    @Query("UPDATE Card c SET c.isDeleted = true WHERE c.id = :cardId")
    void softDeleteCardById(@Param("cardId") Long id);
}
