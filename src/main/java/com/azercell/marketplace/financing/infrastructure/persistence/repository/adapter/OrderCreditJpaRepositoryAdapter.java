package com.azercell.marketplace.financing.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.financing.application.port.OrderCreditRepository;
import com.azercell.marketplace.financing.domain.aggregate.OrderCredit;
import com.azercell.marketplace.financing.infrastructure.persistence.mapper.OrderCreditMapper;
import com.azercell.marketplace.financing.infrastructure.persistence.repository.OrderCreditJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderCreditJpaRepositoryAdapter implements OrderCreditRepository {
    private final OrderCreditJpaRepository orderCreditJpaRepository;

    @Override
    public OrderCredit save(OrderCredit orderCredit) {
        return OrderCreditMapper.toDomain(orderCreditJpaRepository.save(OrderCreditMapper.toJpaEntity(orderCredit)));
    }

    @Override
    public Optional<OrderCredit> findById(UUID id) {
        return orderCreditJpaRepository.findById(id).map(OrderCreditMapper::toDomain);
    }

    @Override
    public Optional<OrderCredit> findByOrderId(UUID orderId) {
        return orderCreditJpaRepository.findByOrderId(orderId).map(OrderCreditMapper::toDomain);
    }

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return orderCreditJpaRepository.existsByOrderId(orderId);
    }
}