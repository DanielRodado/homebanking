package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return args ->  {
			LocalDate date = LocalDate.now();

			Client clientOne = new Client("Melba", "Morel", "melbamorel@gmail.com");
			clientRepository.save(clientOne);

			Account accountOne = new Account("VIN001", date, 5000.00);
			clientOne.addAccount(accountOne);
			accountRepository.save(accountOne);

			Account accountTwo = new Account("VIN002", date.plusDays(1), 7000.00);
			clientOne.addAccount(accountTwo);
			accountRepository.save(accountTwo);
		};
	}

}
