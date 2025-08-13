package br.com.tais.order_management_api.repository;

import br.com.tais.order_management_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
