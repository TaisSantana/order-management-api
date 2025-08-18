package br.com.tais.order_management_api;

import br.com.tais.order_management_api.exception.CustomerNotFoundException;
import br.com.tais.order_management_api.exception.EmailAlreadyExistsException;
import br.com.tais.order_management_api.exception.UsernameAlreadyExistsException;
import br.com.tais.order_management_api.exception.WeakPasswordException;
import br.com.tais.order_management_api.model.Customer;
import br.com.tais.order_management_api.model.dto.CustomerRequestDTO;
import br.com.tais.order_management_api.model.dto.CustomerResponseDTO;
import br.com.tais.order_management_api.model.enums.Roles;
import br.com.tais.order_management_api.repository.CustomerRepository;
import br.com.tais.order_management_api.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CustomerRequestDTO customerRequestDTO;
    private Customer customer;
    private Set<Roles> newRoles;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        newRoles = new HashSet<>(Collections.singletonList(Roles.CUSTOMER));
        customerRequestDTO = new CustomerRequestDTO("John Doe", "johndoe", "password123", "123456789", null,newRoles);
        customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .username("johndoe")
                .email("email@example.com")
                .phone("123456789")
                .password("encodedPassword")
                .roles(new HashSet<>(Set.of(Roles.CUSTOMER))) // Exemplo de roles
                .build();
    }

    @Test
    public void testCreateCustomer_Success() {
        when(customerRepository.findByEmail(customerRequestDTO.email())).thenReturn(Optional.empty());
        when(customerRepository.findByUsername(customerRequestDTO.username())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(customerRequestDTO.password())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO response = customerService.createCustomer(customerRequestDTO);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomer_EmailAlreadyExists() {
        when(customerRepository.findByEmail(customerRequestDTO.email())).thenReturn(Optional.of(customer));

        assertThrows(EmailAlreadyExistsException.class, () -> customerService.createCustomer(customerRequestDTO));
    }

    @Test
    public void testCreateCustomer_UsernameAlreadyExists() {
        when(customerRepository.findByEmail(customerRequestDTO.email())).thenReturn(Optional.empty());
        when(customerRepository.findByUsername(customerRequestDTO.username())).thenReturn(Optional.of(customer));

        assertThrows(UsernameAlreadyExistsException.class, () -> customerService.createCustomer(customerRequestDTO));
    }

    @Test
    public void testCreateCustomer_WeakPassword() {
        customerRequestDTO = new CustomerRequestDTO("John Doe", "johndoe", "123", "123456789", null,newRoles); // Senha fraca

        assertThrows(WeakPasswordException.class, () -> customerService.createCustomer(customerRequestDTO));
    }

    @Test
    public void testPartialUpdate_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerRequestDTO updates = new CustomerRequestDTO("John Doe Updated", "johndoe", null, null, null,newRoles);
        CustomerResponseDTO response = customerService.partialUpdate(1L, updates);

        assertNotNull(response);
        assertEquals("John Doe Updated", response.name());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testPartialUpdate_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        CustomerRequestDTO updates = new CustomerRequestDTO("John Doe Updated", "johndoe", null, null, null,newRoles);
        assertThrows(CustomerNotFoundException.class, () -> customerService.partialUpdate(1L, updates));
    }

    @Test
    public void testGetCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponseDTO response = customerService.getCustomerById(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
    }

    @Test
    public void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    public void testDeleteCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    public void testDeleteCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> customerService.deleteCustomer(1L));
    }
}
