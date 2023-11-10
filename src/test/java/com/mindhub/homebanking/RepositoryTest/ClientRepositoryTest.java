package com.mindhub.homebanking.RepositoryTest;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private List<Client> clients;

    private Client client;

    @BeforeEach
    public void getAnyDataOfAnyClients() {
        clients = clientRepository.findAll();
        client = clientRepository.findByEmail("melbamorel@gmail.com");
    }

    @Test
    public void existsClients(){
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void atLeastOneClient(){
        assertThat(clients, hasItem(hasProperty("lastName", is("Rodado"))));
    }

}
