package com.nm.novamart.Repository;

import com.nm.novamart.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByOrderId(String orderId);

    List<Order> getOrdersByUser_Id(UUID userId);

    boolean existsByOrderId(String orderId);
}
