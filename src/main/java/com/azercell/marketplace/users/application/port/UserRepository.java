package com.azercell.marketplace.users.application.port;

import com.azercell.marketplace.users.domain.aggregate.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByAdObjectId(String adObjectId);
}