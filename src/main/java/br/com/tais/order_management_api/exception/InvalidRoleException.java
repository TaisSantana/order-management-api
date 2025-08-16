package br.com.tais.order_management_api.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String role, String allowedRoles) {
        super("Role inválida: '" + role + "'. Opções válidas: " + allowedRoles);
    }
}
