package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository) {
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

			Loan loanMortgage = new Loan("Mortgage", 500000.00, List.of(12, 24, 36, 48, 60));
			loanRepository.save(loanMortgage);

			Loan loanPersonal = new Loan("Personal", 100000.00, List.of(6, 12, 24));
			loanRepository.save(loanPersonal);

			Loan loanAuto = new Loan("Auto", 300000.00, List.of(6, 12, 24, 36));
			loanRepository.save(loanAuto);

			ClientLoan clientLoanOne = new ClientLoan(400000.00, 60);

			loanMortgage.addClientLoan(clientLoanOne);
			clientOne.addClientLoan(clientLoanOne);
			clientLoanRepository.save(clientLoanOne);

			ClientLoan clientLoanTwo = new ClientLoan(50000.00, 12);

			loanPersonal.addClientLoan(clientLoanTwo);
			clientOne.addClientLoan(clientLoanTwo);
			clientLoanRepository.save(clientLoanTwo);


			// Segundoo cliente
			Client clientTwo = new Client("Daniel", "Rodado", "d4nielrodado@gmail.com");
			clientRepository.save(clientTwo);

			Account accountOneClientTwo = new Account("VIN003", date, 200.00);
			clientTwo.addAccount(accountOneClientTwo);
			accountRepository.save(accountOneClientTwo);

			ClientLoan clientLoanOneClientTwo = new ClientLoan(100000.00, 24);

			loanPersonal.addClientLoan(clientLoanOneClientTwo);
			clientTwo.addClientLoan(clientLoanOneClientTwo);
			clientLoanRepository.save(clientLoanOneClientTwo);

			ClientLoan clientLoanTwoClientTwo = new ClientLoan(200000.00, 36);

			loanAuto.addClientLoan(clientLoanTwoClientTwo);
			clientTwo.addClientLoan(clientLoanTwoClientTwo);
			clientLoanRepository.save(clientLoanTwoClientTwo);
		};
	}

}

/*
Hipotecario: monto máximo 500.000, cuotas 12,24,36,48,60.
Personal: monto máximo 100.000, cuotas 6,12,24
Automotriz: monto máximo 300.000, cuotas 6,12,24,36
		*/
