package com.azercell.marketplace.content.infrastructure.persistence.mapper;

import com.azercell.marketplace.content.domain.aggregate.FaqEntry;
import com.azercell.marketplace.content.infrastructure.persistence.entity.FaqEntryJpaEntity;

import java.util.Optional;

public class FaqEntryMapper {

    public static Optional<FaqEntry> toDomain(FaqEntryJpaEntity entity) {
        if (entity == null) return Optional.empty();
        return Optional.of(FaqEntry.rehydrate(
                entity.getId(),
                entity.getQuestionAz(),
                entity.getQuestionEn(),
                entity.getAnswerAz(),
                entity.getAnswerEn(),
                entity.getFaqGroup(),
                entity.getDisplayOrder(),
                entity.isActive()));
    }

    /**
     * Maps scalars to a fresh entity. Saving by id is a merge; createdDate/createdBy are
     * {@code @Column(updatable = false)}, so audit fields survive updates.
     */
    public static FaqEntryJpaEntity toJpaEntity(FaqEntry faq) {
        var entity = new FaqEntryJpaEntity();
        entity.setId(faq.getId());
        entity.setQuestionAz(faq.getQuestionAz());
        entity.setQuestionEn(faq.getQuestionEn());
        entity.setAnswerAz(faq.getAnswerAz());
        entity.setAnswerEn(faq.getAnswerEn());
        entity.setFaqGroup(faq.getFaqGroup());
        entity.setDisplayOrder(faq.getDisplayOrder());
        entity.setActive(faq.isActive());
        return entity;
    }
}
