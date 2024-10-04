package com.example.sms.infrastructure.repository.user;

import com.example.sms.domain.model.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository {
    Optional<User> findById(String userId);

    List<User> selectAll();

    void save(User user);

    void update(User user);

    void deleteById(String userId);

    void deleteAll();
}
