package br.com.tais.order_management_api.model.dto;

import br.com.tais.order_management_api.model.enums.Roles;

import java.util.Set;

public record CustomerResponseDTO(
        Long id,
        String name,
        String username,
        String email,
        String phone,
        Set<Roles> roles
) {}