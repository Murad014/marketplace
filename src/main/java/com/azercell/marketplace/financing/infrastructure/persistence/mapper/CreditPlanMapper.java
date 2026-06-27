package com.azercell.marketplace.financing.infrastructure.persistence.mapper;

import com.azercell.marketplace.financing.domain.aggregate.CreditPlan;
import com.azercell.marketplace.financing.domain.vo.InterestRate;
import com.azercell.marketplace.financing.infrastructure.persistence.entity.CreditPlanJpaEntity;

public class CreditPlanMapper {

    public static CreditPlanJpaEntity toJpaEntity(CreditPlan plan) {
        var entity = new CreditPlanJpaEntity();
        entity.setId(plan.getId());
        entity.setName(plan.getName());
        entity.setMonths(plan.getMonths());
        entity.setInterestRate(plan.getInterestRate().value());
        entity.setType(plan.getType());
        entity.setStatus(plan.getStatus());
        return entity;
    }

    public static CreditPlan toDomain(CreditPlanJpaEntity entity) {
        return CreditPlan.rehydrate(
                entity.getId(),
                entity.getName(),
                entity.getMonths(),
                new InterestRate(entity.getInterestRate()),
                entity.getType(),
                entity.getStatus());
    }
}