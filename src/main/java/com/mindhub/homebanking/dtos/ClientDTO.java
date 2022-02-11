package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/*El DTO es el encargado de mostrar los datos en pantalla
* Si falta alguna variable setter o getter no se mostrará lo creado.*/
public class ClientDTO {
    /*Variables*/
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    //Un cliente tiene muchas cuentas
    private Set<AccountDTO> accounts = new HashSet<>();
    //Un cliente tiene muchos clientesLoans, prestamos
    private Set<ClientLoanDTO> loans = new HashSet<>();
    //Un cliente tiene muchas cards, tarjetas
    private Set<CardDTO> cards = new HashSet<>();
    //Un cliente tiene muchos seguros
    private Set<ClientSeguroDTO> seguros = new HashSet<>();


    /*Constructores*/
     public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.loans = client.getLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
        this.seguros = client.getSeguros().stream().map(ClientSeguroDTO::new).collect(Collectors.toSet());
    }


    /*Setters y Getters*/
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public void setLoans(Set<ClientLoanDTO> loans) {
        this.loans = loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public void setCards(Set<CardDTO> cards) {
        this.cards = cards;
    }

    public Set<ClientSeguroDTO> getSeguros() {
        return seguros;
    }

    public void setSeguros(Set<ClientSeguroDTO> seguros) {
        this.seguros = seguros;
    }
}
