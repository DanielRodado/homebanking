package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static com.mindhub.homebanking.utils.TransactionUtil.formattedLocalDateTime;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<String> addLoan(@RequestBody LoanApplicationDTO loanApplication, Authentication currentClient) {

        if (loanApplication.getAmount() <= 0) {
            return new ResponseEntity<>("The requested amount may not be less than or equal to 0", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getPayments() <= 0) {
            return new ResponseEntity<>("Quotas cannot be less than or equal to 0", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.getNumberAccountTo().isBlank()) {
            return new ResponseEntity<>("A target account is required", HttpStatus.FORBIDDEN);
        }

        if (!loanService.existsLoanById(loanApplication.getIdLoan())) {
            return new ResponseEntity<>("The requested loan does not exist, request a valid one", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.getLoanById(loanApplication.getIdLoan());

        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The amount chosen cannot exceed the maximum loan amount requested", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplication.getPayments())) {
            return new ResponseEntity<>("The chosen installments are not available in the type of loan requested", HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsAccountByNumber(loanApplication.getNumberAccountTo())) {
            return new ResponseEntity<>("The destination account entered does not exist, enter a valid account", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (!accountService.existsAccountByClientAndNumber(client, loanApplication.getNumberAccountTo())) {
            return new ResponseEntity<>("The destination account entered does not belong to you, enter one that belongs to you", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplication.getAmount()*(loan.getInterestRate()/12),
                loanApplication.getPayments());
        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        Account account = accountService.getAccountByNumber(loanApplication.getNumberAccountTo());

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplication.getAmount(),
                account.getBalance() + loanApplication.getAmount(),
                loan.getName() + " loan approved", formattedLocalDateTime(LocalDateTime.now()));
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(loanApplication.getAmount() + account.getBalance());
        accountService.saveAccount(account);

        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/loans/create")
    public ResponseEntity<String> createNewLoan(@RequestParam String nameOfLoan, @RequestParam Double maxAmount,
                                                @RequestParam Double interestRate,
                                                @RequestParam int payment) {

        if (nameOfLoan.isBlank()) {
            return new ResponseEntity<>("The loan name cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if (loanService.existsLoanByName(nameOfLoan)) {
            return new ResponseEntity<>("Loan name already exists, enter another name", HttpStatus.FORBIDDEN);
        }

        if (maxAmount <= 0) {
            return new ResponseEntity<>("Maximum loan amount cannot be less than or equal to 0", HttpStatus.FORBIDDEN);
        }

        if (interestRate <= 0) {
            return new ResponseEntity<>("The interest rate cannot be less than or equal to 0.", HttpStatus.FORBIDDEN);
        }

        if (payment <= 0) {
            return new ResponseEntity<>("Loan payments cannot be less or equal to 0", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(nameOfLoan, maxAmount, interestRate, List.of(payment));
        loanService.saveLoan(loan);

        return new ResponseEntity<>("New loan created!", HttpStatus.CREATED);
    }

    @GetMapping("/loans")
    public Set<LoanDTO> getAllLoans() {
        return loanService.getAllLoansDTO();
    }
}
