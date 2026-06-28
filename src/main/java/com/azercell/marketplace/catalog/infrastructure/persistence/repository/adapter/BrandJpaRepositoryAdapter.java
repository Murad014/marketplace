package com.azercell.marketplace.catalog.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.catalog.application.port.BrandRepository;
import com.azercell.marketplace.catalog.domain.aggregate.Brand;
import com.azercell.marketplace.catalog.infrastructure.persistence.mapper.BrandMapper;
import com.azercell.marketplace.catalog.infrastructure.persistence.repository.BrandJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BrandJpaRepositoryAdapter implements BrandRepository {
    private final BrandJpaRepository repository;

    @Override
    public List<Brand> getAllBranch() {
        return BrandMapper.toDomainList(repository.findAll());
    }

    @Override
    public Optional<Brand> getBrandById(UUID id) {
        var brandEntity = repository.findById(id);
        return brandEntity.flatMap(BrandMapper::toDomain);
    }

    @Override
    public Brand save(Brand brand) {
        // Mapping to a fresh entity is safe for updates: BaseEntity's createdDate/createdBy are
        // @Column(updatable = false), so a merge by id keeps the original audit fields.
        var saved = repository.save(BrandMapper.toJpaEntity(brand));
        return BrandMapper.toDomain(saved).orElseThrow();
    }

    @Override
    public Optional<Brand> findByName(String name) {
        return repository.findByNameIgnoreCase(name).flatMap(BrandMapper::toDomain);
    }

    @Override
    public Optional<Brand> findByCode(String code) {
        return repository.findByCodeIgnoreCase(code).flatMap(BrandMapper::toDomain);
    }

    // <editor-fold desc="privateHelperMethods">

    // </editor-fold>
}
