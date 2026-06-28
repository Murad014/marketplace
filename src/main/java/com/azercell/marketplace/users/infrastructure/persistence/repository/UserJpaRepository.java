package com.azercell.marketplace.users.infrastructure.persistence.repository;

import com.azercell.marketplace.users.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByAdObjectId(String adObjectId);
}