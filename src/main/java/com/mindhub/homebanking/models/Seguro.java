package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Seguro {
    /* Variables */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private double valor;

    //1 seguro asociado a muchos Client-seguros
    @OneToMany(mappedBy = "seguro", fetch = FetchType.EAGER)
    private Set<ClientSeguro> clientSeguros = new HashSet<>();

    /*Constructores*/

    public Seguro() {
    }

    public Seguro(String name, double valor) {
        this.name = name;
        this.valor = valor;
    }

    /* Setters y Getters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Set<ClientSeguro> getClientSeguros() {
        return clientSeguros;
    }

    public void setClientSeguros(Set<ClientSeguro> clientSeguros) {
        this.clientSeguros = clientSeguros;
    }
}
