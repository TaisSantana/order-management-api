package br.com.tais.order_management_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 1, max = 150, message = "O nome deve ter entre 1 e 150 caracteres")
    @Column(nullable = false, length = 150)
    private String name;

    @Size(min = 1, max = 300, message = "A descrição do produto deve ter entre 1 e 300 caracteres")
    @Column(nullable = true, length = 300)
    private String description;

    @DecimalMin(value = "0.01", inclusive = true, message = "O preço deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Formato de preço inválido (máx 10 dígitos inteiros e 2 decimais)")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    private Integer stock;

}
