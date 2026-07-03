package com.azercell.marketplace.content.infrastructure.persistence.entity;

import com.azercell.marketplace.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "faq_entries")
public class FaqEntryJpaEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "question_az", nullable = false, columnDefinition = "text")
    private String questionAz;

    @Column(name = "question_en", nullable = false, columnDefinition = "text")
    private String questionEn;

    @Column(name = "answer_az", nullable = false, columnDefinition = "text")
    private String answerAz;

    @Column(name = "answer_en", nullable = false, columnDefinition = "text")
    private String answerEn;

    @Column(name = "faq_group", length = 120)
    private String faqGroup;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(nullable = false)
    private boolean active;
}
