package com.azercell.marketplace.content.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * FAQ entry aggregate — an admin-authored question/answer shown on the public storefront.
 * Content is stored bilingually (AZ + EN); the public read picks the locale-appropriate text.
 * {@code displayOrder} controls the listing order, {@code faqGroup} is an optional section label,
 * and {@code active} gates public visibility (admins still see inactive entries).
 */
@Getter
public class FaqEntry {

    private final UUID id;
    private String questionAz;
    private String questionEn;
    private String answerAz;
    private String answerEn;
    private String faqGroup;   // optional section label, e.g. "Orders"; null = ungrouped
    private int displayOrder;
    private boolean active;

    private FaqEntry(UUID id, String questionAz, String questionEn, String answerAz, String answerEn,
                     String faqGroup, int displayOrder, boolean active) {
        this.id = validateId(id);
        this.questionAz = requireQuestion(questionAz);
        this.questionEn = requireQuestion(questionEn);
        this.answerAz = requireAnswer(answerAz);
        this.answerEn = requireAnswer(answerEn);
        this.faqGroup = normalizeGroup(faqGroup);
        this.displayOrder = displayOrder;
        this.active = active;
    }

    public static FaqEntry create(String questionAz, String questionEn, String answerAz, String answerEn,
                                  String faqGroup, Integer displayOrder) {
        return new FaqEntry(UUID.randomUUID(), questionAz, questionEn, answerAz, answerEn,
                faqGroup, displayOrder == null ? 0 : displayOrder, true);
    }

    public static FaqEntry rehydrate(UUID id, String questionAz, String questionEn, String answerAz, String answerEn,
                                     String faqGroup, int displayOrder, boolean active) {
        return new FaqEntry(id, questionAz, questionEn, answerAz, answerEn, faqGroup, displayOrder, active);
    }

    public void changeQuestion(String questionAz, String questionEn) {
        this.questionAz = requireQuestion(questionAz);
        this.questionEn = requireQuestion(questionEn);
    }

    public void changeAnswer(String answerAz, String answerEn) {
        this.answerAz = requireAnswer(answerAz);
        this.answerEn = requireAnswer(answerEn);
    }

    public void changeGroup(String faqGroup) {
        this.faqGroup = normalizeGroup(faqGroup);
    }

    public void changeDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    // <editor-fold desc="privateHelperMethods">
    private static UUID validateId(UUID id) {
        if (id == null) throw new DomainException(ErrorCode.FAQ_ID_REQUIRED);
        return id;
    }

    private static String requireQuestion(String question) {
        if (question == null || question.isBlank())
            throw new DomainException(ErrorCode.FAQ_QUESTION_REQUIRED);
        return question.trim();
    }

    private static String requireAnswer(String answer) {
        if (answer == null || answer.isBlank())
            throw new DomainException(ErrorCode.FAQ_ANSWER_REQUIRED);
        return answer.trim();
    }

    private static String normalizeGroup(String group) {
        return (group == null || group.isBlank()) ? null : group.trim();
    }
    // </editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FaqEntry other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
