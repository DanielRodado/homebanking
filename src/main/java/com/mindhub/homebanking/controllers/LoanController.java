package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    public LocalDateTime formattedLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        return LocalDateTime.parse(formattedDateTime, formatter);
    }

//  @RequestMapping(value="/loans", method=RequestMethod.POST)
    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<String> addLoan(@RequestBody LoanApplicationDTO loanApplication, Authentication currentClient) {

        Client client = clientRepository.findByEmail(currentClient.getName());

        if (loanApplication.getAmount() <= 0) {
            return new ResponseEntity<>("The requested amount may not be less than or equal to 0", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getPayments() <= 0) {
            return new ResponseEntity<>("Quotas cannot be less than or equal to 0", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getNumberAccountTo().isBlank()) {
            return new ResponseEntity<>("A target account is required", HttpStatus.FORBIDDEN);
        }

        if (!loanRepository.existsById(loanApplication.getIdLoan())) {
            return new ResponseEntity<>("The requested loan does not exist, request a valid one", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanRepository.findById(loanApplication.getIdLoan()).orElse(null);

        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The amount chosen cannot exceed the maximum loan amount requested", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplication.getPayments())) {
            return new ResponseEntity<>("The chosen installments are not available in the type of loan requested", HttpStatus.FORBIDDEN);
        }

        if (!accountRepository.existsByNumber(loanApplication.getNumberAccountTo())) {
            return new ResponseEntity<>("The destination account entered does not exist, enter a valid account", HttpStatus.FORBIDDEN);
        }

        Account account = accountRepository.findByNumber(loanApplication.getNumberAccountTo());

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("The destination account entered does not belong to you, enter one that belongs to you", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplication.getAmount()*1.2, loanApplication.getPayments());
        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        clientLoanRepository.save(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplication.getAmount(),
                loan.getName() + " loan approved", formattedLocalDateTime(LocalDateTime.now()));
        account.addTransaction(transaction);
        transactionRepository.save(transaction);

        account.setBalance(loanApplication.getAmount() + account.getBalance());
        accountRepository.save(account);

        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }

    @RequestMapping("/loans")
    public Set<LoanDTO> getAllLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }
}
