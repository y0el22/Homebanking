package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientSeguro {
    /* variables */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private double valor;

    //Muchos seguros(client-seguros) son asociados a un cliente(client)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    //Muchos prestamos(client-loan) son asociados a un seguro(seguros)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seguro_id")
    private Seguro seguro;

    /*Constructores*/

    public ClientSeguro() {
    }

    public ClientSeguro(double valor, Client client, Seguro seguro) {
        this.valor = valor;
        this.client = client;
        this.seguro = seguro;
    }

    /* Setters y Getters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Seguro getSeguro() {
        return seguro;
    }

    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }
}
