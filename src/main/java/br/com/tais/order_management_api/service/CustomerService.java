package br.com.tais.order_management_api.service;

import br.com.tais.order_management_api.exception.CustomerNotFoundException;
import br.com.tais.order_management_api.exception.EmailAlreadyExistsException;
import br.com.tais.order_management_api.exception.UsernameAlreadyExistsException;
import br.com.tais.order_management_api.exception.WeakPasswordException;
import br.com.tais.order_management_api.model.Customer;
import br.com.tais.order_management_api.model.dto.CustomerRequestDTO;
import br.com.tais.order_management_api.model.dto.CustomerResponseDTO;
import br.com.tais.order_management_api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    //injeção de dependencia via construtor - em conjunto com a notation @RequiredArgsConstructor
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {

        validateEmailUnique(customerRequestDTO.email());
        validateUsernameUnique(customerRequestDTO.username());
        validatePassword(customerRequestDTO.password());

        Customer customer = Customer.builder()
                .name(customerRequestDTO.name())
                .username(customerRequestDTO.username())
                .phone(customerRequestDTO.phone())
                .email(customerRequestDTO.email())
                .password(passwordEncoder.encode(customerRequestDTO.password()))
                .roles(customerRequestDTO.roles())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(savedCustomer);
    }

    public CustomerResponseDTO partialUpdate(Long id, CustomerRequestDTO updates) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (updates.username() != null) {
            validateUsernameUniqueForUpdate(updates.username(), id);
            customer.setUsername(updates.username());
        }
        if (updates.email() != null) {
            validateEmailUniqueForUpdate(updates.email(), id);
            customer.setEmail(updates.email());
        }
        if (updates.password() != null) {
            validatePassword(updates.password());
            customer.setPassword(updates.password());
        }
        if (updates.name() != null) customer.setName(updates.name());
        if (updates.phone() != null) customer.setPhone(updates.phone());
        if (updates.roles() != null && !updates.roles().isEmpty()) customer.setRoles(updates.roles());

        return mapToResponseDTO(customerRepository.save(customer));
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com id: " + id));
        return mapToResponseDTO(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com id: " + id));
        customerRepository.delete(customer);
    }

    private void validateEmailUnique(String email) {
        customerRepository.findByEmail(email)
                .ifPresent(c -> { throw new EmailAlreadyExistsException(email); });
    }

    private void validateEmailUniqueForUpdate(String email, Long id) {
        customerRepository.findByEmail(email)
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new EmailAlreadyExistsException(email); });
    }

    private void validateUsernameUnique(String username) {
        customerRepository.findByUsername(username)
                .ifPresent(c -> { throw new UsernameAlreadyExistsException(username); });
    }

    private void validateUsernameUniqueForUpdate(String username, Long id) {
        customerRepository.findByUsername(username)
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new UsernameAlreadyExistsException(username); });
    }

    private void validatePassword(String password) {
        if (password.length() < 6) {
            throw new WeakPasswordException();
        }
        // to do: depois incluir regras de complexidade, caracteres especiais, etc.
    }

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getRoles()
        );
    }

}
