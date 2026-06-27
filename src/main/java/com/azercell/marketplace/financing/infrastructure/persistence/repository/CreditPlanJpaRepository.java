package com.azercell.marketplace.financing.infrastructure.persistence.repository;

import com.azercell.marketplace.financing.domain.vo.CreditPlanType;
import com.azercell.marketplace.financing.domain.vo.Status;
import com.azercell.marketplace.financing.infrastructure.persistence.entity.CreditPlanJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditPlanJpaRepository extends JpaRepository<CreditPlanJpaEntity, UUID> {
    List<CreditPlanJpaEntity> findByStatusOrderByMonthsAsc(Status status);
    List<CreditPlanJpaEntity> findByStatusAndTypeOrderByMonthsAsc(Status status, CreditPlanType type);
    Optional<CreditPlanJpaEntity> findByName(String name);
    boolean existsByIdAndStatus(UUID id, Status status);
}