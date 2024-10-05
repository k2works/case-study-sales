package com.example.sms.infrastructure.datasource.system.user;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.repository.system.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        return userEntity.map(userObjMapper::mapToDomainEntity);
    }

    @Override
    public List<User> selectAll() {
        List<Usr> userEntities = userMapper.selectAll();
        return userEntities.stream()
                .map(userObjMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    public void save(User user) {
        Usr userEntity = userObjMapper.mapToEntity(user);
        if (userMapper.selectByPrimaryKey(user.getUserId()) != null) {
            userMapper.updateByPrimaryKey(userEntity);
            return;
        }
        userMapper.insert(userEntity);
    }

    @Override
    public void update(User user) {
        Usr userEntity = userObjMapper.mapToEntity(user);
        userMapper.updateByPrimaryKey(userEntity);
    }

    @Override
    public void deleteById(String userId) {
        userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public void deleteAll() {
        userMapper.deleteAll();
    }
}
