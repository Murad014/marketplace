package com.azercell.marketplace.financing.application.port;

import com.azercell.marketplace.financing.domain.aggregate.OrderCredit;

import java.util.Optional;
import java.util.UUID;

public interface OrderCreditRepository {
    OrderCredit save(OrderCredit orderCredit);
    Optional<OrderCredit> findById(UUID id);
    Optional<OrderCredit> findByOrderId(UUID orderId);
    boolean existsByOrderId(UUID orderId);
}
