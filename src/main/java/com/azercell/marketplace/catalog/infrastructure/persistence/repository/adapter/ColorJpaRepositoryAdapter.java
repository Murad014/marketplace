package com.azercell.marketplace.catalog.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.catalog.application.port.ColorRepository;
import com.azercell.marketplace.catalog.domain.Color;
import com.azercell.marketplace.catalog.infrastructure.persistence.mapper.ColorMapper;
import com.azercell.marketplace.catalog.infrastructure.persistence.repository.ColorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ColorJpaRepositoryAdapter implements ColorRepository {
    private final ColorJpaRepository colorJpaRepository;

    @Override
    public Optional<Color> findById(UUID id) {
        var fromDb = colorJpaRepository.findById(id);
        return fromDb.map(ColorMapper::toDomain);
    }

    @Override
    public Optional<Color> findByName(String name) {
        return colorJpaRepository.findByName(name).map(ColorMapper::toDomain);
    }

    @Override
    public Color save(Color color) {
        var saved = colorJpaRepository.save(ColorMapper.toJpaEntity(color));
        return ColorMapper.toDomain(saved);
    }
}
