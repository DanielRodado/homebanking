package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Set<Transaction> getAllTransactionsByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    @Override
    public void deletedTransactions(Account account) {
        Set<Transaction> transactions = getAllTransactionsByAccount(account);
        for (Transaction transaction: transactions) {
            transaction.setDeleted(true);
            saveTransaction(transaction);
        }
    }


    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
