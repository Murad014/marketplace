package com.azercell.marketplace.financing.infrastructure.persistence.mapper;

import com.azercell.marketplace.financing.domain.Installment;
import com.azercell.marketplace.financing.domain.aggregate.OrderCredit;
import com.azercell.marketplace.financing.infrastructure.persistence.entity.InstallmentJpaEntity;
import com.azercell.marketplace.financing.infrastructure.persistence.entity.OrderCreditJpaEntity;

import java.util.ArrayList;

public class OrderCreditMapper {

    public static OrderCreditJpaEntity toJpaEntity(OrderCredit credit) {
        var entity = new OrderCreditJpaEntity();
        entity.setId(credit.getId());
        entity.setOrderId(credit.getOrderId());
        entity.setCreditPlanId(credit.getCreditPlanId());
        entity.setMonths(credit.getMonths());
        entity.setInterestRate(credit.getInterestRate());
        entity.setPrincipalAmount(credit.getPrincipalAmount());
        entity.setInterestAmount(credit.getInterestAmount());
        entity.setTotalPayable(credit.getTotalPayable());
        entity.setInstallmentAmount(credit.getInstallmentAmount());
        entity.setPaidAmount(credit.getPaidAmount());
        entity.setStatus(credit.getStatus());
        entity.setStartedAt(credit.getStartedAt());

        var installments = credit.getInstallments().stream()
                .map(OrderCreditMapper::toInstallmentEntity)
                .toList();
        installments.forEach(i -> i.setOrderCredit(entity));
        entity.setInstallments(new ArrayList<>(installments));
        return entity;
    }

    public static OrderCredit toDomain(OrderCreditJpaEntity entity) {
        var installments = entity.getInstallments().stream()
                .map(OrderCreditMapper::toInstallmentDomain)
                .toList();
        return OrderCredit.rehydrate(
                entity.getId(),
                entity.getOrderId(),
                entity.getCreditPlanId(),
                entity.getMonths(),
                entity.getInterestRate(),
                entity.getPrincipalAmount(),
                entity.getInterestAmount(),
                entity.getTotalPayable(),
                entity.getInstallmentAmount(),
                entity.getPaidAmount(),
                entity.getStatus(),
                entity.getStartedAt(),
                installments);
    }

    private static InstallmentJpaEntity toInstallmentEntity(Installment i) {
        var e = new InstallmentJpaEntity();
        e.setId(i.getId());
        e.setSequenceNo(i.getSequenceNo());
        e.setDueDate(i.getDueDate());
        e.setAmount(i.getAmount());
        e.setStatus(i.getStatus());
        e.setPaidAmount(i.getPaidAmount());
        e.setPaidAt(i.getPaidAt());
        return e;
    }

    private static Installment toInstallmentDomain(InstallmentJpaEntity e) {
        return Installment.rehydrate(
                e.getId(),
                e.getSequenceNo(),
                e.getDueDate(),
                e.getAmount(),
                e.getStatus(),
                e.getPaidAmount(),
                e.getPaidAt());
    }
}