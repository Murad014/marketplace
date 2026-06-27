package com.azercell.marketplace.financing.application.service.impl;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.financing.application.port.CreditPlanRepository;
import com.azercell.marketplace.financing.application.port.OrderCreditRepository;
import com.azercell.marketplace.financing.application.service.OrderCreditService;
import com.azercell.marketplace.financing.domain.Installment;
import com.azercell.marketplace.financing.domain.aggregate.OrderCredit;
import com.azercell.marketplace.financing.web.dto.response.OrderCreditResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCreditServiceImpl implements OrderCreditService {

    private final OrderCreditRepository orderCreditRepository;
    private final CreditPlanRepository creditPlanRepository;

    @Override
    @Transactional
    public OrderCreditResponse createForOrder(UUID orderId, UUID creditPlanId, BigDecimal principal) {
        if (orderCreditRepository.existsByOrderId(orderId))
            throw new DomainException(ErrorCode.ORDER_CREDIT_ALREADY_EXISTS);

        var plan = creditPlanRepository.findById(creditPlanId)
                .orElseThrow(() -> new DomainException(ErrorCode.CREDIT_PLAN_NOT_FOUND));

        var credit = OrderCredit.create(orderId, plan.getId(), plan.getMonths(),
                plan.getInterestRate().value(), principal);
        return toResponse(orderCreditRepository.save(credit));
    }

    @Override
    @Transactional
    public OrderCreditResponse getByOrderId(UUID orderId) {
        return toResponse(orderCreditRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DomainException(ErrorCode.ORDER_CREDIT_NOT_FOUND)));
    }

    @Override
    @Transactional
    public OrderCreditResponse getById(UUID id) {
        return toResponse(orderCreditRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.ORDER_CREDIT_NOT_FOUND)));
    }

    @Override
    @Transactional
    public OrderCreditResponse payNextInstallment(UUID orderCreditId) {
        var credit = orderCreditRepository.findById(orderCreditId)
                .orElseThrow(() -> new DomainException(ErrorCode.ORDER_CREDIT_NOT_FOUND));
        credit.payNextInstallment();
        return toResponse(orderCreditRepository.save(credit));
    }

    private OrderCreditResponse toResponse(OrderCredit c) {
        var installments = c.getInstallments().stream()
                .sorted((a, b) -> Integer.compare(a.getSequenceNo(), b.getSequenceNo()))
                .map(this::toInstallmentView)
                .toList();

        return new OrderCreditResponse(
                c.getId(), c.getOrderId(), c.getCreditPlanId(), c.getMonths(), c.getInterestRate(),
                c.getPrincipalAmount(), c.getInterestAmount(), c.getTotalPayable(), c.getInstallmentAmount(),
                c.getPaidAmount(), c.getStatus().name(), c.getStartedAt(), installments);
    }

    private OrderCreditResponse.InstallmentView toInstallmentView(Installment i) {
        return new OrderCreditResponse.InstallmentView(
                i.getId(), i.getSequenceNo(), i.getDueDate(), i.getAmount(),
                i.getStatus().name(), i.getPaidAmount(), i.getPaidAt());
    }
}