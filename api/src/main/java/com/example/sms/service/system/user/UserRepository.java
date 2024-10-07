package com.example.sms.service.system.user;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserList;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String userId);

    UserList selectAll();

    void save(User user);

    void update(User user);

    void deleteById(String userId);

    void deleteAll();
}
