package br.com.tais.order_management_api.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {

    @NotBlank(message = "A rua é obrigatória")
    @Size(max = 255, message = "A rua deve ter no máximo 255 caracteres")
    private String street;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
    private String city;

    @NotBlank(message = "O estado é obrigatório")
    @Size(max = 100, message = "O estado deve ter no máximo 100 caracteres")
    private String state;

    @NotBlank(message = "O CEP é obrigatório")
    @Size(max = 20, message = "O CEP deve ter no máximo 20 caracteres")
    private String zipCode;

    @NotBlank(message = "O país é obrigatório")
    @Size(max = 100, message = "O país deve ter no máximo 100 caracteres")
    private String country;

}
