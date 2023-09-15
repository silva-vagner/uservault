package com.uservault.exception;

public class UserReferenceMissingException extends ApiException {
    public UserReferenceMissingException() {
        super(400, "Necessário preencher email ou userId!");
    }
}
