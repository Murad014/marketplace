package com.azercell.marketplace.content.infrastructure.persistence.repository;

import com.azercell.marketplace.content.infrastructure.persistence.entity.FaqEntryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FaqEntryJpaRepository extends JpaRepository<FaqEntryJpaEntity, UUID> {

    List<FaqEntryJpaEntity> findAllByOrderByDisplayOrderAscCreatedDateAsc();

    List<FaqEntryJpaEntity> findAllByActiveTrueOrderByDisplayOrderAscCreatedDateAsc();
}
