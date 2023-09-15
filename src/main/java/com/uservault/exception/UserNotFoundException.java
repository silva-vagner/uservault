package com.uservault.exception;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException() {
        super(404, "Usuário não encontrado!");
    }
}
