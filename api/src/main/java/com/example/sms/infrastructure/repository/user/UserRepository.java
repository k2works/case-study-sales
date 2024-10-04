package com.example.sms.infrastructure.repository.user;

import com.example.sms.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String userId);
}
