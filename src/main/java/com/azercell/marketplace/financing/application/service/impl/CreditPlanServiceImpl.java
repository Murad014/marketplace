package com.azercell.marketplace.financing.application.service.impl;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.financing.application.port.CreditPlanRepository;
import com.azercell.marketplace.financing.application.service.CreditPlanService;
import com.azercell.marketplace.financing.domain.aggregate.CreditPlan;
import com.azercell.marketplace.financing.domain.vo.CreditPlanType;
import com.azercell.marketplace.financing.domain.vo.InterestRate;
import com.azercell.marketplace.financing.web.dto.request.CreateCreditPlanRequest;
import com.azercell.marketplace.financing.web.dto.request.UpdateCreditPlanRequest;
import com.azercell.marketplace.financing.web.dto.response.CreditPlanResponse;
import com.azercell.marketplace.financing.web.dto.response.InstallmentOptionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditPlanServiceImpl implements CreditPlanService {

    private final CreditPlanRepository creditPlanRepository;

    @Override
    @Transactional
    public CreditPlanResponse create(CreateCreditPlanRequest request) {
        var type = request.type() == null ? CreditPlanType.OPTIONAL : request.type();
        var plan = CreditPlan.create(request.name(), request.months(),
                new InterestRate(request.interestRate()), type);
        return toResponse(creditPlanRepository.save(plan));
    }

    @Override
    @Transactional
    public CreditPlanResponse update(UUID id, UpdateCreditPlanRequest request) {
        var plan = load(id);
        plan.changeName(request.name());
        plan.changeMonths(request.months());
        plan.changeInterestRate(new InterestRate(request.interestRate()));
        plan.changeType(request.type());
        return toResponse(creditPlanRepository.save(plan));
    }

    @Override
    @Transactional
    public CreditPlanResponse getById(UUID id) {
        return toResponse(load(id));
    }

    @Override
    @Transactional
    public List<CreditPlanResponse> list() {
        return creditPlanRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public CreditPlanResponse activate(UUID id) {
        var plan = load(id);
        plan.activate();
        return toResponse(creditPlanRepository.save(plan));
    }

    @Override
    @Transactional
    public CreditPlanResponse deactivate(UUID id) {
        var plan = load(id);
        plan.deactivate();
        return toResponse(creditPlanRepository.save(plan));
    }

    @Override
    @Transactional
    public List<InstallmentOptionResponse> quote(BigDecimal price) {
        return creditPlanRepository.findAllActive().stream()
                .map(plan -> new InstallmentOptionResponse(
                        plan.getId(),
                        plan.getName(),
                        plan.getMonths(),
                        plan.getInterestRate().value(),
                        plan.monthlyInstallment(price),
                        plan.interestAmount(price),
                        plan.totalPayable(price)))
                .toList();
    }

    private CreditPlan load(UUID id) {
        return creditPlanRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.CREDIT_PLAN_NOT_FOUND));
    }

    private CreditPlanResponse toResponse(CreditPlan p) {
        return new CreditPlanResponse(p.getId(), p.getName(), p.getMonths(),
                p.getInterestRate().value(), p.getType().name(), p.getStatus().name());
    }
}