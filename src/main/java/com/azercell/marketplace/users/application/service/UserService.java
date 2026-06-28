package com.azercell.marketplace.users.application.service;

import com.azercell.marketplace.users.application.ProvisionUserCommand;
import com.azercell.marketplace.users.domain.aggregate.User;

import java.util.UUID;

public interface UserService {

    /** Find the local user by adObjectId and sync profile fields, or create it on first sight (JIT). */
    User provision(ProvisionUserCommand command);

    User getById(UUID id);
}