package br.com.tais.order_management_api.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Cliente não encontrado com id: " + id);
    }
}
