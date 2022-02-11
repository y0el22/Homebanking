package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Seguro;

public class SeguroDTO {
    private Long id;
    private String name;
    private double valor;

    public SeguroDTO(Seguro seguro) {
        this.id = seguro.getId();
        this.name = seguro.getName();
        this.valor = seguro.getValor();
    }

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
}
