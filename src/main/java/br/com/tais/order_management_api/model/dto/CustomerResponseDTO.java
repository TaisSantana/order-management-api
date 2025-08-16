package br.com.tais.order_management_api.model.dto;

public record CustomerResponseDTO(
        Long id,
        String name,
        String username,
        String email
) {}