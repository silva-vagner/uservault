package com.uservault.exception;

public class EmailNotFoundException extends ApiException {
    public EmailNotFoundException() {
        super(404, "Email não encontrado");
    }
}
