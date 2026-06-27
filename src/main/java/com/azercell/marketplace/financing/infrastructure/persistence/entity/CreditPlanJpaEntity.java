package com.azercell.marketplace.financing.infrastructure.persistence.entity;

import com.azercell.marketplace.common.entity.BaseEntity;
import com.azercell.marketplace.financing.domain.vo.CreditPlanType;
import com.azercell.marketplace.financing.domain.vo.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "credit_plans")
public class CreditPlanJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private int months;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CreditPlanType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;
}
