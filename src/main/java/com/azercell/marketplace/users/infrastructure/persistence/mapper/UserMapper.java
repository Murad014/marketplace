package com.azercell.marketplace.users.infrastructure.persistence.mapper;

import com.azercell.marketplace.users.domain.aggregate.User;
import com.azercell.marketplace.users.infrastructure.persistence.entity.UserJpaEntity;

public class UserMapper {

    public static UserJpaEntity toJpaEntity(User user) {
        var e = new UserJpaEntity();
        e.setId(user.getId());
        e.setAdObjectId(user.getAdObjectId());
        e.setEmployeeId(user.getEmployeeId());
        e.setEmail(user.getEmail());
        e.setFullName(user.getFullName());
        e.setDepartment(user.getDepartment());
        e.setRole(user.getRole());
        e.setActive(user.isActive());
        return e;
    }

    public static User toDomain(UserJpaEntity e) {
        return User.rehydrate(e.getId(), e.getAdObjectId(), e.getEmployeeId(), e.getEmail(),
                e.getFullName(), e.getDepartment(), e.getRole(), e.isActive());
    }
}