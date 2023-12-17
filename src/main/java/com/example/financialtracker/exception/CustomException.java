package com.example.financialtracker.exception;

public class CustomException extends RuntimeException {
    private int status;

    public CustomException(String error, int status) {
        super(error);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}