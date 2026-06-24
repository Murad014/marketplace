package com.azercell.marketplace.catalog.infrastructure.persistence.entity;
import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
public class ColorJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 60)
    private String name;

    @Column(name = "hex_code", length = 7)
    private String hexCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

}