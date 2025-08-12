package br.com.tais.order_management_api.repository;

import br.com.tais.order_management_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
