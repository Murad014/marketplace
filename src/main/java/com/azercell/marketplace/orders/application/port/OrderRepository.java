package com.azercell.marketplace.orders.application.port;

import com.azercell.marketplace.orders.domain.aggregate.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    Page<Order> findByUser(UUID userId, Pageable pageable);
    Page<Order> findAll(Pageable pageable);
}