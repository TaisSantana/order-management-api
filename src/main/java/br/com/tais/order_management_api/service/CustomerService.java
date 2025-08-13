package br.com.tais.order_management_api.service;

import br.com.tais.order_management_api.model.Customer;
import br.com.tais.order_management_api.model.dto.CustomerDTO;
import br.com.tais.order_management_api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    //injeção de dependencia via construtor - em conjunto com a notation @RequiredArgsConstructor
    private final CustomerRepository customerRepository;

    public Customer createCustomer(CustomerDTO customerDTO){

        customerRepository.findByEmail(customerDTO.getEmail())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("E-mail já cadastrado: " + customerDTO.getEmail());
                });

        Customer costumer = Customer.builder()
                .name(customerDTO.getName())
                .phone(customerDTO.getPhone())
                .email(customerDTO.getEmail())
                .password(customerDTO.getPassword())
                .build();

        return customerRepository.save(costumer);

    }

}
