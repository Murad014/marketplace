package com.azercell.marketplace.financing.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.financing.application.port.CreditPlanRepository;
import com.azercell.marketplace.financing.domain.aggregate.CreditPlan;
import com.azercell.marketplace.financing.domain.vo.CreditPlanType;
import com.azercell.marketplace.financing.domain.vo.Status;
import com.azercell.marketplace.financing.infrastructure.persistence.mapper.CreditPlanMapper;
import com.azercell.marketplace.financing.infrastructure.persistence.repository.CreditPlanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CreditPlanJpaRepositoryAdapter implements CreditPlanRepository {
    private final CreditPlanJpaRepository creditPlanJpaRepository;

    @Override
    public CreditPlan save(CreditPlan plan) {
        var saved = creditPlanJpaRepository.save(CreditPlanMapper.toJpaEntity(plan));
        return CreditPlanMapper.toDomain(saved);
    }

    @Override
    public Optional<CreditPlan> findById(UUID id) {
        return creditPlanJpaRepository.findById(id).map(CreditPlanMapper::toDomain);
    }

    @Override
    public Optional<CreditPlan> findByName(String name) {
        return creditPlanJpaRepository.findByName(name).map(CreditPlanMapper::toDomain);
    }

    @Override
    public List<CreditPlan> findAll() {
        return creditPlanJpaRepository.findAll().stream().map(CreditPlanMapper::toDomain).toList();
    }

    @Override
    public List<CreditPlan> findAllActive() {
        return creditPlanJpaRepository.findByStatusOrderByMonthsAsc(Status.ACTIVE).stream()
                .map(CreditPlanMapper::toDomain)
                .toList();
    }

    @Override
    public List<CreditPlan> findActiveDefaults() {
        return creditPlanJpaRepository
                .findByStatusAndTypeOrderByMonthsAsc(Status.ACTIVE, CreditPlanType.DEFAULT).stream()
                .map(CreditPlanMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsActive(UUID id) {
        return creditPlanJpaRepository.existsByIdAndStatus(id, Status.ACTIVE);
    }
}