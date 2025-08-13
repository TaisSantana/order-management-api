package br.com.tais.order_management_api.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Roles {
    CUSTOMER("ROLE_CUSTOMER", "Cliente comum"),
    STAFF("ROLE_STAFF", "Funcionário"),
    ADMIN("ROLE_ADMIN", "Administrador total"),
    SALES("ROLE_SALES", "Equipe de vendas"),
    INVENTORY_MANAGER("ROLE_INVENTORY_MGR", "Gestor de estoque"),
    PREMIUM_CUSTOMER("ROLE_PREMIUM", "Cliente VIP"),
    SUPPORT("ROLE_SUPPORT", "Atendimento"),
    MARKETING("ROLE_MARKETING", "Equipe de marketing");

    private final String authority;
    private final String description;
    public static Optional<Roles> fromAuthority(String authority) {
        return Arrays.stream(values())
                .filter(role -> role.authority.equalsIgnoreCase(authority))
                .findFirst();
    }
}