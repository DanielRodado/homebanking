package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository) {
		return args ->  {
			LocalDate date = LocalDate.now();
			LocalDateTime dateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = dateTime.format(formatter);
			LocalDateTime formattedLocalDateTime = LocalDateTime.parse(formattedDateTime, formatter);

			Client clientOne = new Client("Melba", "Morel", "melbamorel@gmail.com");
			clientRepository.save(clientOne);

			Account accountOne = new Account("VIN001", date, 5000.00);
			clientOne.addAccount(accountOne);
			accountRepository.save(accountOne);

			Transaction transactionOne = new Transaction(TransactionType.DEBIT, 8000.00, "My first transaction", formattedLocalDateTime);
			accountOne.addTransaction(transactionOne);
			transactionRepository.save(transactionOne);

            Transaction transactionTwo = new Transaction(TransactionType.DEBIT, 32500.00, "Second transaction", formattedLocalDateTime);
            accountOne.addTransaction(transactionTwo);
            transactionRepository.save(transactionTwo);

			Account accountTwo = new Account("VIN002", date.plusDays(1), 7000.00);
			clientOne.addAccount(accountTwo);
			accountRepository.save(accountTwo);

            Transaction transactionThree = new Transaction(TransactionType.CREDIT, -2000.00, "Payment per transaction",
                    formattedLocalDateTime);
            accountTwo.addTransaction(transactionThree);
            transactionRepository.save(transactionThree);

			Transaction transactionFour = new Transaction(TransactionType.CREDIT, -2000.00, "Payment of school fees",
					formattedLocalDateTime);
			accountOne.addTransaction(transactionFour);
			transactionRepository.save(transactionFour);
		};
	}

}
