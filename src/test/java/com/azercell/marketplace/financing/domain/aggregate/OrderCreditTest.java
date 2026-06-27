package com.azercell.marketplace.financing.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.financing.domain.Installment;
import com.azercell.marketplace.financing.domain.vo.CreditStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Reference TIER 1 — domain unit test. Pure Java: no Spring, no DB, sub-millisecond.
 * This is where the business rules live; pattern the rest of the aggregate tests off this.
 */
class OrderCreditTest {

    private static final UUID ORDER = UUID.randomUUID();
    private static final UUID PLAN = UUID.randomUUID();

    private BigDecimal sumOfInstallments(OrderCredit credit) {
        return credit.getInstallments().stream()
                .map(Installment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Test
    void create_computesFlatInterestTotalAndMonthlyAmount() {
        var credit = OrderCredit.create(ORDER, PLAN, 12, new BigDecimal("5"), new BigDecimal("1200"));

        assertThat(credit.getInterestAmount()).isEqualByComparingTo("60.00");    // 1200 * 5%
        assertThat(credit.getTotalPayable()).isEqualByComparingTo("1260.00");    // 1200 + 60
        assertThat(credit.getInstallmentAmount()).isEqualByComparingTo("105.00"); // 1260 / 12
        assertThat(credit.getInstallments()).hasSize(12);
        assertThat(credit.getStatus()).isEqualTo(CreditStatus.ACTIVE);
        assertThat(credit.getPaidAmount()).isEqualByComparingTo("0.00");
    }

    @Test
    void schedule_sumsToTotalExactly_lastInstallmentAbsorbsRounding() {
        // 100 over 3 months at 0% -> 33.33, 33.33, 33.34  (must still total 100.00)
        var credit = OrderCredit.create(ORDER, PLAN, 3, BigDecimal.ZERO, new BigDecimal("100"));

        var amounts = credit.getInstallments();
        assertThat(amounts.get(0).getAmount()).isEqualByComparingTo("33.33");
        assertThat(amounts.get(2).getAmount()).isEqualByComparingTo("33.34");
        assertThat(sumOfInstallments(credit)).isEqualByComparingTo("100.00");
    }

    @Test
    void installments_areSequencedAndDueMonthly() {
        var credit = OrderCredit.create(ORDER, PLAN, 3, BigDecimal.ZERO, new BigDecimal("90"));

        var first = credit.getInstallments().get(0);
        var third = credit.getInstallments().get(2);
        assertThat(first.getSequenceNo()).isEqualTo(1);
        assertThat(third.getSequenceNo()).isEqualTo(3);
        assertThat(third.getDueDate()).isEqualTo(first.getDueDate().plusMonths(2));
    }

    @Test
    void payNextInstallment_settlesInOrder_andCompletesWhenAllPaid() {
        var credit = OrderCredit.create(ORDER, PLAN, 3, BigDecimal.ZERO, new BigDecimal("99")); // 33 each

        var first = credit.payNextInstallment();
        assertThat(first.getSequenceNo()).isEqualTo(1);
        assertThat(credit.getPaidAmount()).isEqualByComparingTo("33.00");
        assertThat(credit.getStatus()).isEqualTo(CreditStatus.ACTIVE);

        credit.payNextInstallment();
        credit.payNextInstallment();

        assertThat(credit.getPaidAmount()).isEqualByComparingTo("99.00");
        assertThat(credit.getStatus()).isEqualTo(CreditStatus.COMPLETED);
    }

    @Test
    void payingACompletedCredit_isRejected() {
        var credit = OrderCredit.create(ORDER, PLAN, 1, BigDecimal.ZERO, new BigDecimal("50"));
        credit.payNextInstallment(); // completes

        assertThatThrownBy(credit::payNextInstallment)
                .isInstanceOf(DomainException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ORDER_CREDIT_NOT_ACTIVE);
    }

    @Test
    void create_rejectsNonPositivePrincipal() {
        assertThatThrownBy(() -> OrderCredit.create(ORDER, PLAN, 6, new BigDecimal("2"), BigDecimal.ZERO))
                .isInstanceOf(DomainException.class);
    }
}