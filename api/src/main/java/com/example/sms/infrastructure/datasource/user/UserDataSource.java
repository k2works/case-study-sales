package com.example.sms.infrastructure.datasource.user;

import com.example.sms.domain.model.User;
import com.example.sms.infrastructure.repository.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDataSource implements UserRepository {
    final UsrMapper userMapper;
    final UserObjMapper userObjMapper;

    public UserDataSource(UsrMapper userMapper, UserObjMapper userObjMapper) {
        this.userMapper = userMapper;
        this.userObjMapper = userObjMapper;
    }

    @Override
    public Optional<User> findById(String userId) {
        Optional<Usr> userEntity = Optional.ofNullable(userMapper.selectByPrimaryKey(userId));
        return Optional.of(userObjMapper.mapToDomainEntity(userEntity.orElseThrow()));
    }
}
