package com.mindhub.homebanking.models;

//import net.minidev.json.annotate.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Account {
    /*Variables*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    //Muchas cuentas están asociadas a un cliente
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    //Una cuenta tiene muchas transacciones
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    /*Constructores*/
    public Account() {
    }

    public Account(String number, LocalDateTime creationDate, double balance, Client client) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
    }

    /*Getters y Setters*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    //Retorna el String
    public String toString(){
        return number + " " + creationDate + " " + balance;
    }
}
