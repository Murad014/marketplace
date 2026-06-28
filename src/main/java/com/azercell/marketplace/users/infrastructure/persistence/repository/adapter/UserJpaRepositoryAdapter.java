package com.azercell.marketplace.users.infrastructure.persistence.repository.adapter;

import com.azercell.marketplace.users.application.port.UserRepository;
import com.azercell.marketplace.users.domain.aggregate.User;
import com.azercell.marketplace.users.infrastructure.persistence.mapper.UserMapper;
import com.azercell.marketplace.users.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserJpaRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return UserMapper.toDomain(userJpaRepository.save(UserMapper.toJpaEntity(user)));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByAdObjectId(String adObjectId) {
        return userJpaRepository.findByAdObjectId(adObjectId).map(UserMapper::toDomain);
    }
}