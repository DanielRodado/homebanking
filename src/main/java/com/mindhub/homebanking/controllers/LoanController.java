package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.LoanApplicationDTO;
import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.DTO.NewLoanApplicationDTO;
import com.mindhub.homebanking.DTO.PayLoanApplicationDTO;
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

import static com.mindhub.homebanking.Utils.LoanUtil.formattedLocalDateTime;
import static com.mindhub.homebanking.Utils.LoanUtil.formatterStringStartUpperEndLower;

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

        if (loanApplication.amount() <= 0) {
            return new ResponseEntity<>("The requested amount may not be less than or equal to 0", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.payments() <= 0) {
            return new ResponseEntity<>("Quotas cannot be less than or equal to 0", HttpStatus.FORBIDDEN);
        }

        if (loanApplication.numberAccountTo().isBlank()) {
            return new ResponseEntity<>("A target account is required", HttpStatus.FORBIDDEN);
        }

        if (!loanService.existsLoanById(loanApplication.idLoan())) {
            return new ResponseEntity<>("The requested loan does not exist, request a valid one", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.getLoanById(loanApplication.idLoan());

        if (loanApplication.amount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The amount chosen cannot exceed the maximum loan amount requested", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplication.payments())) {
            return new ResponseEntity<>("The chosen installments are not available in the type of loan requested", HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsAccountByNumber(loanApplication.numberAccountTo())) {
            return new ResponseEntity<>("The destination account entered does not exist, enter a valid account", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (!accountService.existsAccountByClientAndNumber(client, loanApplication.numberAccountTo())) {
            return new ResponseEntity<>("The destination account entered does not belong to you, enter one that belongs to you", HttpStatus.FORBIDDEN);
        }

        if (clientLoanService.existsClientLoanByClientAndLoan(client, loan)) {
            return new ResponseEntity<>("You already have one type of "+ loan.getName() + " loan, if you wish to " +
                    "apply for another one, pay the one you already have.",
                    HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan =
                new ClientLoan((double) Math.round(loanApplication.amount()+(loanApplication.amount()*(loan.getInterestRate()/100))),
                        loanApplication.payments());
        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        Account account = accountService.getAccountByNumber(loanApplication.numberAccountTo());

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplication.amount(),
                account.getBalance() + loanApplication.amount(),
                loan.getName() + " loan approved", formattedLocalDateTime(LocalDateTime.now()));
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(loanApplication.amount() + account.getBalance());
        accountService.saveAccount(account);

        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/loans/create")
    public ResponseEntity<String> createNewLoan(@RequestBody NewLoanApplicationDTO newLoanApp) {

        if (newLoanApp.nameOfLoan().isBlank()) {
            return new ResponseEntity<>("The loan name cannot be empty.", HttpStatus.FORBIDDEN);
        }

        String LoanName = formatterStringStartUpperEndLower(newLoanApp.nameOfLoan());

        if (loanService.existsLoanByName(LoanName)) {
            return new ResponseEntity<>("Loan name ('" + LoanName + "') already exists, enter another name.",
                    HttpStatus.FORBIDDEN);
        }

        if (newLoanApp.maxAmount() <= 0) {
            return new ResponseEntity<>("Maximum loan amount cannot be less than or equal to 0.", HttpStatus.FORBIDDEN);
        }

        if (newLoanApp.interestRate() <= 0) {
            return new ResponseEntity<>("The interest rate cannot be less than or equal to 0.", HttpStatus.FORBIDDEN);
        }

        if ((Integer) newLoanApp.payments().size() == 0) {
            return new ResponseEntity<>("At least one payment is required to add it.", HttpStatus.FORBIDDEN);
        }

        int temporaryPayment = 0;
        for (Integer payment: newLoanApp.payments()) {
            if (payment <= 0) {
                return new ResponseEntity<>("Loan payments cannot be less or equal to 0.", HttpStatus.FORBIDDEN);
            }
            if (payment <= temporaryPayment) {
                return new ResponseEntity<>("You cannot enter one payment less than or equal to another, the order must be ascending.",
                        HttpStatus.FORBIDDEN);
            }
            temporaryPayment = payment;
        }

        Loan loan = new Loan(LoanName, newLoanApp.maxAmount(),
                newLoanApp.interestRate(), newLoanApp.payments());
        loanService.saveLoan(loan);

        return new ResponseEntity<>("New loan created!", HttpStatus.CREATED);
    }

    @PostMapping("/loans/pay")
    @Transactional
    public ResponseEntity<String> payLoan(@RequestBody PayLoanApplicationDTO payLoanApp, Authentication currentClient) {

        if (!clientLoanService.existsClientLoanById(payLoanApp.clientLoanId())) {
            return new ResponseEntity<>("The loan does not exist.", HttpStatus.ACCEPTED);
        }

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (!clientLoanService.existsClientLoanByClientAndId(client, payLoanApp.clientLoanId())) {
            return new ResponseEntity<>("This loan does not belong to you", HttpStatus.ACCEPTED);
        }

        if (!accountService.existsAccountByNumber(payLoanApp.accountNumber())) {
            return new ResponseEntity<>("Account number " + payLoanApp.accountNumber() + " does not exist.",
                    HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsAccountByClientAndNumber(client, payLoanApp.accountNumber())) {
            return new ResponseEntity<>("The account number " + payLoanApp.accountNumber() + " does not belong to you.",
                    HttpStatus.FORBIDDEN);
        }

        if (accountService.existsAccountByNumberAndBalanceLessThan(
                payLoanApp.accountNumber(), payLoanApp.amountToPay())) {
            return new ResponseEntity<>("Insufficient amount to pay", HttpStatus.FORBIDDEN);
        }

        if (clientLoanService.existsClientLoanByIdAndPaymentsLessThan(
                payLoanApp.clientLoanId(), payLoanApp.payments())) {
            return new ResponseEntity<>("Payments received exceed loan repayments.", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = clientLoanService.getClientLoanById(payLoanApp.clientLoanId());

        if (clientLoanService.existsClientLoanByIdAndAmountLessThan(
                payLoanApp.clientLoanId(), clientLoan.getAmountMade() + payLoanApp.amountToPay())) {
            return new ResponseEntity<>("The amount paid in exceeds the amount of the loan.", HttpStatus.FORBIDDEN);
        }

        clientLoan.setPayments(clientLoan.getPayments() - payLoanApp.payments());
        clientLoan.setPaymentsMade(clientLoan.getPaymentsMade() + payLoanApp.payments());
        clientLoan.setAmountMade(clientLoan.getAmountMade() + payLoanApp.amountToPay());

        Account account = accountService.getAccountByNumber(payLoanApp.accountNumber());

        Transaction transaction = new Transaction(TransactionType.DEBIT, -payLoanApp.amountToPay(),
                account.getBalance() - payLoanApp.amountToPay(),
                payLoanApp.payments() + " loan installments paid.", formattedLocalDateTime(LocalDateTime.now()));

        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance() - payLoanApp.amountToPay());
        accountService.saveAccount(account);

        if (clientLoanService.existsClientLoanByIdAndPayments(payLoanApp.clientLoanId(), 0)) {
            clientLoanService.deleteClientLoan(clientLoan);
        }

        return new ResponseEntity<>("The payment was successful!", HttpStatus.ACCEPTED);
    }

    @GetMapping("/loans")
    public Set<LoanDTO> getAllLoans() {
        return loanService.getAllLoansDTO();
    }

}
