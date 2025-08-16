package br.com.tais.order_management_api.model;

import br.com.tais.order_management_api.model.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @NotBlank
    @Size(min = 1, max = 100, message = "O nome deve ter entre 1 e 100 caracteres")
    @Column(nullable = false, length = 100)
    String name;

    @NotBlank
    @Size(min = 1, max = 100, message = "O nome de usuário deve ter entre 1 e 100 caracteres")
    @Column(nullable = false, length = 100)
    String username;

    @Email(message = "O e-mail deve ser válido")
    @NotBlank(message = "O e-mail é obrigatório")
    @Column(unique = true, nullable = false)
    String email;

    @Size(max = 15)
    private String phone;

    @Valid
    @Embedded
    private Address address;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_roles")
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;
}
