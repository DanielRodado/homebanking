package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> newCard(@RequestParam Double amount, @RequestParam String description,
                                          @RequestParam String numberOfAccountFrom,
                                          @RequestParam String numberOfAccountTo, Authentication currentClient) {

        Client client = clientRepository.findByEmail(currentClient.getName());
        Account accountFrom = accountRepository.findByNumber(numberOfAccountFrom);
        Account accountTo = accountRepository.findByNumber(numberOfAccountTo);

        // Verificar que los parámetros no estén vacíos
        if (numberOfAccountFrom.isEmpty() || numberOfAccountFrom.isBlank())
            return new ResponseEntity<>("AA source account is required.", HttpStatus.FORBIDDEN);

        if (numberOfAccountTo.isEmpty() || numberOfAccountTo.isBlank())
            return new ResponseEntity<>("A target account is required.", HttpStatus.FORBIDDEN);

        if (amount <= 0)
            return new ResponseEntity<>("A transaction cannot be made with an amount less than or equal to 0", HttpStatus.FORBIDDEN);

        if (description.isEmpty() || description.isBlank())
            return new ResponseEntity<>("Fill in the description field.", HttpStatus.FORBIDDEN);

        if (description.length() > 100)
            return new ResponseEntity<>("The description is too long (can´t be longer than 100 characters)",
                    HttpStatus.FORBIDDEN);

        // Verifica que los números de cuenta no sean iguales
        if (numberOfAccountFrom.equals(numberOfAccountTo))
            return new ResponseEntity<>("A transaction cannot be made to the same account.", HttpStatus.FORBIDDEN);

        // Verifica que exista la cuenta de origen
        if (!accountRepository.existsByNumber(numberOfAccountFrom))
            return new ResponseEntity<>("The source account number does not exist.", HttpStatus.FORBIDDEN);

        // Verifica que la cuenta de origen pertenezca al cliente autenticado
        if (!client.getAccounts().contains(accountFrom))
            return new ResponseEntity<>("The account of origin does not belong to you.", HttpStatus.FORBIDDEN);

        // Verifica que exista la cuenta de destino
        if (!accountRepository.existsByNumber(numberOfAccountTo))
            return new ResponseEntity<>("The destination account number does not exist.", HttpStatus.FORBIDDEN);

        // Verifica que la cuenta de origen tenga el monto disponible.
        if (accountFrom.getBalance() < amount)
            return new ResponseEntity<>("Your account does not have enough amount to perform the transaction.", HttpStatus.FORBIDDEN);

        // Se instancian las transacciones con sus datos
        Transaction transactionFrom = new Transaction(TransactionType.DEBIT, -amount,
                description + " To " + numberOfAccountTo,
                LocalDateTime.now());
        Transaction transactionTo = new Transaction(TransactionType.CREDIT, amount,
                description + " To " + numberOfAccountFrom,
                LocalDateTime.now());

        accountFrom.addTransaction(transactionFrom);
        accountTo.addTransaction(transactionTo);

        transactionRepository.save(transactionFrom);
        transactionRepository.save(transactionTo);

        // Se le resta al balance de la cuenta de origen y se le suma a la cuenta de destino
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(amount + accountTo.getBalance());

        /*accountRepository.save(accountFrom);
        accountRepository.save(accountTo);*/

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
