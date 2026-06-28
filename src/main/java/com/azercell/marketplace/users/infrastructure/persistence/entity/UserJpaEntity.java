package com.azercell.marketplace.users.infrastructure.persistence.entity;

import com.azercell.marketplace.common.entity.BaseEntity;
import com.azercell.marketplace.users.domain.vo.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class UserJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "ad_object_id", nullable = false, unique = true, length = 100)
    private String adObjectId;

    @Column(name = "employee_id", unique = true, length = 50)
    private String employeeId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(length = 120)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(nullable = false)
    private boolean active;
}