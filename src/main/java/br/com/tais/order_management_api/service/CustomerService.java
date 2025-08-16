package br.com.tais.order_management_api.service;

import br.com.tais.order_management_api.model.Customer;
import br.com.tais.order_management_api.model.dto.CustomerRequestDTO;
import br.com.tais.order_management_api.model.dto.CustomerResponseDTO;
import br.com.tais.order_management_api.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    //injeção de dependencia via construtor - em conjunto com a notation @RequiredArgsConstructor
    private final CustomerRepository customerRepository;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {

        customerRepository.findByEmail(customerRequestDTO.email())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("E-mail já cadastrado: " + customerRequestDTO.email());
                });

        Customer customer = Customer.builder()
                .name(customerRequestDTO.name())
                .username(customerRequestDTO.username())
                .phone(customerRequestDTO.phone())
                .email(customerRequestDTO.email())
                .password(customerRequestDTO.password())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(savedCustomer);
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com id: " + id));

        customer.setName(customerRequestDTO.name());
        customer.setPhone(customerRequestDTO.phone());
        customer.setEmail(customerRequestDTO.email());
        customer.setPassword(customerRequestDTO.password());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(updatedCustomer);
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

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }

}
