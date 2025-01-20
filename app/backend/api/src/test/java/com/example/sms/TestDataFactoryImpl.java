package com.example.sms;

import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.service.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataFactoryImpl implements TestDataFactory {
    @Autowired
    UserRepository userRepository;

    @Override
    public void setUpForAuthApiService() {
        userRepository.deleteAll();
        userRepository.save(getUser());
        userRepository.save(getAdmin());
    }

    @Override
    public void setUpForUserManagementService() {
        userRepository.deleteAll();
        userRepository.save(getUser());
        userRepository.save(getAdmin());
    }

    public static User getUser() {
        return User.of("U999999", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }

    public static User getAdmin() {
        return User.of("U888888", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }
}
