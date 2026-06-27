package com.azercell.marketplace.orders.infrastructure.persistence.entity;

import com.azercell.marketplace.common.entity.BaseEntity;
import com.azercell.marketplace.orders.domain.vo.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_status_history", indexes = {
        @Index(name = "idx_osh_order", columnList = "order_id")
})
public class OrderStatusHistoryJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 20)
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 20)
    private OrderStatus toStatus;

    @Column(length = 255)
    private String note;

    @Column(name = "changed_by")
    private UUID changedBy;
}
