package com.aninfo.service;

import com.aninfo.model.Transaction;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    public Transaction deposit(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }

    public Transaction withdraw(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByCbu(Long cbu){
        return this.transactionRepository.findAllByCbu(cbu);
    }

    public Transaction findTransactionByID(Long id){

        return this.transactionRepository.findByidTransaction(id);
    }


    public void deleteTransaction(Transaction transaction) {
        transactionRepository.deleteById(transaction.getIdTransaction());
        }
    }
