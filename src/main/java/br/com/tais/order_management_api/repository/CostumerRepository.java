package br.com.tais.order_management_api.repository;

import br.com.tais.order_management_api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostumerRepository extends JpaRepository<Customer,Long> {

    Customer findByEmail(String email);

}
