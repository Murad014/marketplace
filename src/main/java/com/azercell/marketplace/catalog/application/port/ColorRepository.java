package com.azercell.marketplace.catalog.application.port;

import com.azercell.marketplace.catalog.domain.Color;

import java.util.Optional;
import java.util.UUID;

public interface ColorRepository {
    Optional<Color> findById(UUID id);
}
