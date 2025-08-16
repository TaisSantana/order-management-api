package br.com.tais.order_management_api.model.dto;

import br.com.tais.order_management_api.model.enums.Roles;
import br.com.tais.order_management_api.validation.OnCreate;
import br.com.tais.order_management_api.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.EnumSet;
import java.util.Set;

public record CustomerRequestDTO(

        @NotBlank(message = "O nome é obrigatório", groups = OnCreate.class)
        @Size(min = 1, max = 100, message = "O nome deve ter entre 1 e 100 caracteres")
        String name,

        @NotBlank(message = "O nome de usuário é obrigatório",groups = OnCreate.class)
        String username,

        @NotBlank(message = "A senha é obrigatória",groups = OnCreate.class)
        String password,

        @NotBlank(message = "O email é obrigatório",groups = OnCreate.class)
        @Email(message = "Email inválido",groups = {OnCreate.class, OnUpdate.class})
        String email,

        @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres",groups = {OnCreate.class, OnUpdate.class})
        String phone,

        Set<Roles> roles
) {
    public CustomerRequestDTO {
        if (roles == null) {
            roles = EnumSet.noneOf(Roles.class);
        }
    }
}