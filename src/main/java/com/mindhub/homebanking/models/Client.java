package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Client {
    /*Variables*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    //Un cliente tiene muchas cuentas
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();
    //Un cliente tiene muchos prestamos (clien-loans)
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();
    //Un cliente tiene muchas tarjeas (cards)
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();
    //Un cliente tiene muchos seguros
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientSeguro> seguros = new HashSet<>();


    /*Constructor vacío*/
    //Tengo entendido que es necesario para la BD, lo mismo que Firestore
    public Client() {
    }

    /*Constructor*/
    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /*Getters y Setters*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts(){
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    //El método setAccounts recibe un set completo de accounts
    //Para añadir solo una cuenta a la lista se crea este método add:
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    public Set<ClientLoan> getLoans() {
        return loans;
    }

    public void setLoans(Set<ClientLoan> loans) {
        this.loans = loans;
    }

    //Este método recibe un solo clienLoan y lo añade a la lista
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        loans.add(clientLoan);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    //Método add, para agregar las tarjetas de una en una y no pasar un hash completo
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

    public Set<ClientSeguro> getSeguros() {
        return seguros;
    }

    public void setSeguros(Set<ClientSeguro> seguros) {
        this.seguros = seguros;
    }

    //Retorna el String
    public String toString(){
        return firstName + " " + lastName + " " + email;
    }
}
