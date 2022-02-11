package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.ClientSeguro;

public class ClientSeguroDTO {
    private Long id;
    private double valor;
    private Long seguroId;
    private String name;

    public ClientSeguroDTO(ClientSeguro clientSeguro){
        this.id = clientSeguro.getId();
        this.valor = clientSeguro.getValor();
        this.seguroId = clientSeguro.getSeguro().getId();
        this.name = clientSeguro.getSeguro().getName();
    }

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

    public Long getSeguroId() {
        return seguroId;
    }

    public void setSeguroId(Long seguroId) {
        this.seguroId = seguroId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
