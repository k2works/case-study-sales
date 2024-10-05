package com.example.sms;

import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.repository.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataFactoryImpl implements TestDataFactory {
    @Autowired
    UserRepository userRepository;

    @Override
    public void setUpForAuthApiService() {
        userRepository.deleteAll();
        userRepository.save(user());
        userRepository.save(admin());
    }

    @Override
    public User User() {
        return user();
    }

    @Override
    public void setUpForUserManagementService() {
        userRepository.deleteAll();
        userRepository.save(user());
        userRepository.save(admin());
    }

    private static User user() {
        return new User("user", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }

    private static User admin() {
        return new User("admin", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }
}
