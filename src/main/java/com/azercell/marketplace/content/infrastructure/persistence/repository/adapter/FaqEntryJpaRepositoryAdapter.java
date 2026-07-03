package com.azercell.marketplace.content.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.content.application.port.FaqRepository;
import com.azercell.marketplace.content.domain.aggregate.FaqEntry;
import com.azercell.marketplace.content.infrastructure.persistence.mapper.FaqEntryMapper;
import com.azercell.marketplace.content.infrastructure.persistence.repository.FaqEntryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FaqEntryJpaRepositoryAdapter implements FaqRepository {

    private final FaqEntryJpaRepository faqJpaRepository;

    @Override
    public FaqEntry save(FaqEntry faq) {
        var saved = faqJpaRepository.save(FaqEntryMapper.toJpaEntity(faq));
        return FaqEntryMapper.toDomain(saved).orElseThrow();
    }

    @Override
    public Optional<FaqEntry> findById(UUID id) {
        return faqJpaRepository.findById(id).flatMap(FaqEntryMapper::toDomain);
    }

    @Override
    public List<FaqEntry> findAll() {
        return faqJpaRepository.findAllByOrderByDisplayOrderAscCreatedDateAsc().stream()
                .map(FaqEntryMapper::toDomain)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public List<FaqEntry> findAllActive() {
        return faqJpaRepository.findAllByActiveTrueOrderByDisplayOrderAscCreatedDateAsc().stream()
                .map(FaqEntryMapper::toDomain)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (id == null || !faqJpaRepository.existsById(id)) return false;
        faqJpaRepository.deleteById(id);
        return true;
    }
}
