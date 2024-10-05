package com.example.sms.infrastructure.repository.system.user;

import com.example.sms.domain.model.system.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String userId);

    List<User> selectAll();

    void save(User user);

    void update(User user);

    void deleteById(String userId);

    void deleteAll();
}
