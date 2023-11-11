package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.util.List;
import java.util.Set;

public interface TransactionService {

    Set<Transaction> getAllTransactionsByAccount(Account account);

    void deletedTransactions(Account account);

    void saveTransaction(Transaction transaction);

}
