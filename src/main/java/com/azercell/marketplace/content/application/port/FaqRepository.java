package com.azercell.marketplace.content.application.port;

import com.azercell.marketplace.content.domain.aggregate.FaqEntry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FaqRepository {

    FaqEntry save(FaqEntry faq);

    Optional<FaqEntry> findById(UUID id);

    /** Admin view: every entry (active + inactive), ordered by displayOrder then creation time. */
    List<FaqEntry> findAll();

    /** Public view: only active entries, ordered by displayOrder then creation time. */
    List<FaqEntry> findAllActive();

    /** Deletes by id; returns false if nothing existed to delete. */
    boolean deleteById(UUID id);
}
