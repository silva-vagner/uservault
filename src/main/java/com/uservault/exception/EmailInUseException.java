package com.uservault.exception;

public class EmailInUseException extends ApiException {
    public EmailInUseException() {
        super(409, "Email já está em uso!");
    }
}
