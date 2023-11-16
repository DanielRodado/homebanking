package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.dto.NewLoanApplication;
import com.mindhub.homebanking.dto.PayLoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

import static com.mindhub.homebanking.utils.LoanUtil.formatterStringStartUpperEndLower;

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

        if (clientLoanService.existsClientLoanByClientAndLoan(client, loan)) {
            return new ResponseEntity<>("You already have one type of "+ loan.getName() + " loan, if you wish to " +
                    "apply for another one, pay the one you already have.",
                    HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan =
                new ClientLoan((double) Math.round(loanApplication.getAmount()+(loanApplication.getAmount()*(loan.getInterestRate()/100))),
                loanApplication.getPayments());
        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        Account account = accountService.getAccountByNumber(loanApplication.getNumberAccountTo());

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplication.getAmount(),
                account.getBalance() + loanApplication.getAmount(),
                loan.getName() + " loan approved", LocalDateTime.now());
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(loanApplication.getAmount() + account.getBalance());
        accountService.saveAccount(account);

        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/loans/create")
    public ResponseEntity<String> createNewLoan(@RequestBody NewLoanApplication newLoanApp) {

        if (newLoanApp.getNameOfLoan().isBlank()) {
            return new ResponseEntity<>("The loan name cannot be empty.", HttpStatus.FORBIDDEN);
        }

        String LoanName = formatterStringStartUpperEndLower(newLoanApp.getNameOfLoan());

        if (loanService.existsLoanByName(LoanName)) {
            return new ResponseEntity<>("Loan name ('" + LoanName + "') already exists, enter another name.",
                    HttpStatus.FORBIDDEN);
        }

        if (newLoanApp.getMaxAmount() <= 0) {
            return new ResponseEntity<>("Maximum loan amount cannot be less than or equal to 0.", HttpStatus.FORBIDDEN);
        }

        if (newLoanApp.getInterestRate() <= 0) {
            return new ResponseEntity<>("The interest rate cannot be less than or equal to 0.", HttpStatus.FORBIDDEN);
        }

        if ((Integer) newLoanApp.getPayments().size() == 0) {
            return new ResponseEntity<>("At least one payment is required to add it.", HttpStatus.FORBIDDEN);
        }

        int temporaryPayment = 0;
        for (Integer payment: newLoanApp.getPayments()) {
            if (payment <= 0) {
                return new ResponseEntity<>("Loan payments cannot be less or equal to 0.", HttpStatus.FORBIDDEN);
            }
            if (payment < temporaryPayment) {
                return new ResponseEntity<>("You cannot enter one payment less than another, the order must be ascending.",
                        HttpStatus.FORBIDDEN);
            }
            temporaryPayment = payment;
        }

        Loan loan = new Loan(LoanName, newLoanApp.getMaxAmount(),
                             newLoanApp.getInterestRate(), newLoanApp.getPayments());
        loanService.saveLoan(loan);

        return new ResponseEntity<>("New loan created!", HttpStatus.CREATED);
    }

    @PostMapping("/loans/pay")
    @Transactional
    public ResponseEntity<String> payLoan(@RequestBody PayLoanApplicationDTO payLoanApp, Authentication currentClient) {

        if (!clientLoanService.existsClientLoanById(payLoanApp.getClientLoanId())) {
            return new ResponseEntity<>("The loan does not exist.", HttpStatus.ACCEPTED);
        }

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (!clientLoanService.existsClientLoanByClientAndId(client, payLoanApp.getClientLoanId())) {
            return new ResponseEntity<>("This loan does not belong to you", HttpStatus.ACCEPTED);
        }

        if (!accountService.existsAccountByNumber(payLoanApp.getAccountNumber())) {
            return new ResponseEntity<>("Account number " + payLoanApp.getAccountNumber() + " does not exist.", HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsAccountByClientAndNumber(client, payLoanApp.getAccountNumber())) {
            return new ResponseEntity<>("The account number " + payLoanApp.getAccountNumber() + " does not belong to you.",
                    HttpStatus.FORBIDDEN);
        }

        if (accountService.existsAccountByNumberAndBalanceLessThan(
                payLoanApp.getAccountNumber(), payLoanApp.getAmountToPay())) {
            return new ResponseEntity<>("Insufficient amount to pay", HttpStatus.FORBIDDEN);
        }

        if (clientLoanService.existsClientLoanByIdAndPaymentsLessThan(
                payLoanApp.getClientLoanId(), payLoanApp.getPayments())) {
            return new ResponseEntity<>("Payments received exceed loan repayments.", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = clientLoanService.getClientLoanByid(payLoanApp.getClientLoanId());

        if (clientLoanService.existsClientLoanByIdAndAmountLessThan(
                payLoanApp.getClientLoanId(), clientLoan.getAmountMade() + payLoanApp.getAmountToPay())) {
            return new ResponseEntity<>("The amount paid in exceeds the amount of the loan.", HttpStatus.FORBIDDEN);
        }

        clientLoan.setPayments(clientLoan.getPayments() - payLoanApp.getPayments());
        clientLoan.setPaymentsMade(clientLoan.getPaymentsMade() + payLoanApp.getPayments());
        clientLoan.setAmountMade(clientLoan.getAmountMade() + payLoanApp.getAmountToPay());

        Account account = accountService.getAccountByNumber(payLoanApp.getAccountNumber());

        Transaction transaction = new Transaction(TransactionType.DEBIT, -payLoanApp.getAmountToPay(),
                account.getBalance() - payLoanApp.getAmountToPay(),
                payLoanApp.getPayments() + " loan installments paid.", LocalDateTime.now());

        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance() - payLoanApp.getAmountToPay());
        accountService.saveAccount(account);

        if (clientLoanService.existsClientLoanByIdAndPayments(payLoanApp.getClientLoanId(), 0)) {
            clientLoanService.deleteClientLoan(clientLoan);
        }

        return new ResponseEntity<>("The payment was successful!", HttpStatus.ACCEPTED);
    }

    @GetMapping("/loans")
    public Set<LoanDTO> getAllLoans() {
        return loanService.getAllLoansDTO();
    }
}
