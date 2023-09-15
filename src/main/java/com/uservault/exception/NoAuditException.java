package com.uservault.exception;

public class NoAuditException extends ApiException {
    public NoAuditException() {
        super(404, "Nenhum histórico encontrado!");
    }
}
