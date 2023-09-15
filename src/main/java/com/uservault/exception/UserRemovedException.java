package com.uservault.exception;

public class UserRemovedException extends ApiException {
    public UserRemovedException() {
        super(403, "Acesso desativado. Contate o adiministrador!");
    }
}
