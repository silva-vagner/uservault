package com.uservault.exception;

public class NotModifiedException extends ApiException {
    public NotModifiedException() {
        super(204, "Nenhuma alteração aplicada!");
    }
}
