package com.example.financialtracker.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private final int status;

    public CustomException(String error, int status) {
        super(error);
        this.status = status;
    }

}