package com.mindhub.homebanking.dtos;

public class SeguroApplicationDTO {
    private long seguroId;
    private double amount;
    private String toAccountNumber;

    public SeguroApplicationDTO(long seguroId, double amount, String toAccountNumber) {
        this.seguroId = seguroId;
        this.amount = amount;
        this.toAccountNumber = toAccountNumber;
    }

    public long getSeguroId() {
        return seguroId;
    }

    public void setSeguroId(long seguroId) {
        this.seguroId = seguroId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }
}
