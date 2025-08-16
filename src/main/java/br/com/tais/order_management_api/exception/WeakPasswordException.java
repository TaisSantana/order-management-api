package br.com.tais.order_management_api.exception;

public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException() {
        super("A senha não atende aos requisitos de segurança.");
    }
}
