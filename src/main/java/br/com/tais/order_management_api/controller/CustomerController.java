package br.com.tais.order_management_api.controller;

import br.com.tais.order_management_api.model.dto.CustomerRequestDTO;
import br.com.tais.order_management_api.model.dto.CustomerResponseDTO;
import br.com.tais.order_management_api.service.CustomerService;
import br.com.tais.order_management_api.validation.OnCreate;
import br.com.tais.order_management_api.validation.OnUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @Validated(OnCreate.class) @RequestBody CustomerRequestDTO customerRequestDTO) {

        CustomerResponseDTO savedCustomer = customerService.createCustomer(customerRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.id())
                .toUri();

        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> patchCustomer(
            @PathVariable Long id,
            @Validated(OnUpdate.class) @RequestBody CustomerRequestDTO updates) {

        CustomerResponseDTO updatedCustomer = customerService.partialUpdate(id, updates);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
