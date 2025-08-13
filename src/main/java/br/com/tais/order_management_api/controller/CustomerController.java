package br.com.tais.order_management_api.controller;

import br.com.tais.order_management_api.model.Customer;
import br.com.tais.order_management_api.model.dto.CustomerDTO;
import br.com.tais.order_management_api.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
//@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(path = "/saveCustomer")
    public ResponseEntity<Customer> createCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        Customer  savedCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

}
