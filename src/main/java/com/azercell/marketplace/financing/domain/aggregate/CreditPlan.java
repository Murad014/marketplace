package com.azercell.marketplace.financing.domain.aggregate;

import com.azercell.marketplace.financing.domain.vo.InterestRate;
import com.azercell.marketplace.financing.domain.vo.Status;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
public class CreditPlan {
    private final UUID id;
    private String name;
    private int months;
    private InterestRate interestRate;
    private Status status;

    private CreditPlan(
            UUID id,
            String name,
            int months,
            InterestRate interestRate,
            Status status
    ) {
        validateName(name);
        validateInterestRate(interestRate);
        validateMonth(months);

        this.id = id;
        this.name = name;
        this.months = months;
        this.interestRate = interestRate;
        this.status = status;
    }

    public CreditPlan create(String name, int months,
                             InterestRate interestRate, Status status){
        return new CreditPlan(UUID.randomUUID(), name, months, interestRate, status);
    }

    public CreditPlan update(UUID id, String name, int months,
                             InterestRate interestRate, Status status){
        validateId(id);
        return new CreditPlan(id, name, months, interestRate, status);
    }

    public void changeName(String name){
        validateName(name);
        this.name = name;
    }

    public void changeMonths(int months){
        validateMonth(months);
        this.months = months;
    }

    public void changeInterestRate(InterestRate interestRate){
        validateInterestRate(interestRate);
        this.interestRate = interestRate;
    }

    public void makeInActive(){
        this.status = Status.IN_ACTIVE;
    }

    public void makeActive(){
        this.status = Status.ACTIVE;
    }

    public BigDecimal calculatePerMonth(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        BigDecimal interestAmount = price
                .multiply(interestRate.value())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = price.add(interestAmount);

        return totalAmount.divide(
                BigDecimal.valueOf(months),
                2,
                RoundingMode.HALF_UP
        );
    }

    // <editor-fold desc="privateHelperMethod">
    private void validateName(String name) {
        if (name == null || name.isEmpty())
            throw new RuntimeException("Credit plan name cannot be null or empty");
    }

    private void validateMonth(int months) {
        if (months <= 0)
            throw new RuntimeException("Credit Plan months must be greater than zero");
    }

    private void validateInterestRate(InterestRate interestRate) {
        if (interestRate == null)
            throw new RuntimeException("Interest Rate cannot be null");
    }
    private void validateId(UUID id){
        if(id == null)
            throw new RuntimeException("id cannot be null");
    }
    // </editor-fold>
}
