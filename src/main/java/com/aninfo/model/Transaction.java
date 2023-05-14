package com.aninfo.model;
import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTransaction;

    private String type; // "deposit" "withdraw"
    private Double amount;
    private Long cbu;

    public Transaction() {

    }

    public Transaction(String type, Double amount, Long cbu){
        this.type = type;
        this.amount = amount;
        this.cbu = cbu;
    }

    public Double getAmount() {
        return amount;
    }

    public Long getIdTransaction() {
        return idTransaction;
    }

    public String getType() {
        return type;
    }

    public Long getCbu() {
        return cbu;
    }

}
