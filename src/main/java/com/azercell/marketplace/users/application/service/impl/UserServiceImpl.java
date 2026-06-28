package com.azercell.marketplace.users.application.service.impl;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.users.application.ProvisionUserCommand;
import com.azercell.marketplace.users.application.port.UserRepository;
import com.azercell.marketplace.users.application.service.UserService;
import com.azercell.marketplace.users.domain.aggregate.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User provision(ProvisionUserCommand cmd) {
        return userRepository.findByAdObjectId(cmd.adObjectId())
                .map(existing -> {
                    // Dirty-check: only persist when a claim actually differs, so the common
                    // request (nothing changed in Keycloak) is a pure read — no UPDATE.
                    boolean changed = existing.applyIdentity(cmd.employeeId(), cmd.email(),
                            cmd.fullName(), cmd.department(), cmd.role());
                    return changed ? userRepository.save(existing) : existing;
                })
                .orElseGet(() -> userRepository.save(User.provision(
                        cmd.adObjectId(), cmd.employeeId(), cmd.email(), cmd.fullName(),
                        cmd.department(), cmd.role())));
    }

    @Override
    @Transactional
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.USER_NOT_FOUND));
    }
}