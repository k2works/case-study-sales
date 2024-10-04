package com.example.sms.infrastructure.datasource;

import com.example.sms.domain.model.User;
import com.example.sms.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDataSource {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    public Optional<User> findById(String userId) {
        Optional<UserJpaEntity> userJpaEntity = userRepository.findById(userId);
        return Optional.of(userMapper.mapToDomainEntity(userJpaEntity.orElseThrow()));
    }
    public void save(User user) {
        userRepository.save(userMapper.mapToJpaEntity(user));
    }
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
