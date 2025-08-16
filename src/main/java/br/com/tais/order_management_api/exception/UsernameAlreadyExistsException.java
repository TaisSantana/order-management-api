package br.com.tais.order_management_api.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Nome de usuário já cadastrado: " + username);
    }
}
