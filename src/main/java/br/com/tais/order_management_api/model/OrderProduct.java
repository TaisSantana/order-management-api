package br.com.tais.order_management_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProduct{

    @EmbeddedId
    private OrderProductId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    private Integer quantity;

    @DecimalMin(value = "0.01", inclusive = true, message = "O preço deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Formato de preço inválido (máx 10 dígitos inteiros e 2 decimais)")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}
