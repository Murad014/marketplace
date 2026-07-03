package com.azercell.marketplace.content.application.service.impl;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.content.application.port.FaqRepository;
import com.azercell.marketplace.content.application.service.FaqService;
import com.azercell.marketplace.content.domain.aggregate.FaqEntry;
import com.azercell.marketplace.content.web.dto.request.CreateFaqRequest;
import com.azercell.marketplace.content.web.dto.request.UpdateFaqRequest;
import com.azercell.marketplace.content.web.dto.response.FaqAdminResponse;
import com.azercell.marketplace.content.web.dto.response.FaqResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    @Transactional
    public FaqAdminResponse create(CreateFaqRequest request) {
        var faq = FaqEntry.create(request.questionAz(), request.questionEn(),
                request.answerAz(), request.answerEn(), request.group(), request.displayOrder());
        return toAdminResponse(faqRepository.save(faq));
    }

    @Override
    @Transactional
    public FaqAdminResponse update(UUID id, UpdateFaqRequest request) {
        var faq = faqRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.FAQ_NOT_FOUND));

        faq.changeQuestion(request.questionAz(), request.questionEn());
        faq.changeAnswer(request.answerAz(), request.answerEn());
        faq.changeGroup(request.group());
        if (request.displayOrder() != null) faq.changeDisplayOrder(request.displayOrder());
        if (request.active() != null) {
            if (request.active()) faq.activate();
            else faq.deactivate();
        }

        return toAdminResponse(faqRepository.save(faq));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!faqRepository.deleteById(id))
            throw new DomainException(ErrorCode.FAQ_NOT_FOUND);
    }

    @Override
    @Transactional
    public FaqAdminResponse getById(UUID id) {
        return faqRepository.findById(id)
                .map(this::toAdminResponse)
                .orElseThrow(() -> new DomainException(ErrorCode.FAQ_NOT_FOUND));
    }

    @Override
    @Transactional
    public List<FaqAdminResponse> getAll() {
        return faqRepository.findAll().stream()
                .map(this::toAdminResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<FaqResponse> getPublished(Locale locale) {
        boolean az = locale != null && "az".equalsIgnoreCase(locale.getLanguage());
        return faqRepository.findAllActive().stream()
                .map(f -> new FaqResponse(
                        f.getId(),
                        az ? f.getQuestionAz() : f.getQuestionEn(),
                        az ? f.getAnswerAz() : f.getAnswerEn(),
                        f.getFaqGroup(),
                        f.getDisplayOrder()))
                .toList();
    }

    // <editor-fold desc="privateHelperMethods">
    private FaqAdminResponse toAdminResponse(FaqEntry f) {
        return new FaqAdminResponse(
                f.getId(),
                f.getQuestionAz(),
                f.getQuestionEn(),
                f.getAnswerAz(),
                f.getAnswerEn(),
                f.getFaqGroup(),
                f.getDisplayOrder(),
                f.isActive());
    }
    // </editor-fold>
}
