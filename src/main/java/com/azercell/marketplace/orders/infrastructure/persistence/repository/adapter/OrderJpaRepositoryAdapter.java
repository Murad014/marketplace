package com.azercell.marketplace.orders.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.orders.application.port.OrderRepository;
import com.azercell.marketplace.orders.domain.aggregate.Order;
import com.azercell.marketplace.orders.infrastructure.persistence.mapper.OrderMapper;
import com.azercell.marketplace.orders.infrastructure.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderJpaRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return OrderMapper.toDomain(orderJpaRepository.save(OrderMapper.toJpaEntity(order)));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderJpaRepository.findById(id).map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findByUser(UUID userId, Pageable pageable) {
        return orderJpaRepository.findByUserId(userId, pageable).map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderJpaRepository.findAll(pageable).map(OrderMapper::toDomain);
    }
}