package br.com.tais.order_management_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import br.com.tais.order_management_api.exception.InvalidRoleException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Roles {
    CUSTOMER("CUSTOMER", "Cliente comum"),
    STAFF("STAFF", "Funcionário"),
    ADMIN("ADMIN", "Administrador total"),
    SALES("SALES", "Equipe de vendas"),
    INVENTORY_MANAGER("INVENTORY_MANAGER", "Gestor de estoque"),
    PREMIUM_CUSTOMER("PREMIUM_CUSTOMER", "Cliente VIP"),
    SUPPORT("SUPPORT", "Atendimento"),
    MARKETING("MARKETING", "Equipe de marketing");

    private final String authority;
    private final String description;


}