package com.example.sms.infrastructure.datasource.system.user;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.service.system.user.UserRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDataSource implements UserRepository {
    final UsrMapper userMapper;
    final UserEntityMapper userEntityMapper;

    public UserDataSource(UsrMapper userMapper, UserEntityMapper userEntityMapper) {
        this.userMapper = userMapper;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Optional<User> findById(String userId) {
        Optional<Usr> userEntity = Optional.ofNullable(userMapper.selectByPrimaryKey(userId));
        return userEntity.map(userEntityMapper::mapToDomainModel);
    }

    @Override
    public UserList selectAll() {
        List<Usr> userEntities = userMapper.selectAll();
        return new UserList(userEntities.stream()
                .map(userEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public PageInfo<User> selectAllWithPageInfo() {
        List<Usr> userEntities = userMapper.selectAll();
        PageInfo<Usr> pageInfo = new PageInfo<>(userEntities);

        return PageInfoHelper.of(pageInfo, userEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(User user) {
        Usr userEntity = userEntityMapper.mapToEntity(user);
        if (userMapper.selectByPrimaryKey(user.getUserId().Value()) != null) {
            userMapper.updateByPrimaryKey(userEntity);
            return;
        }
        userMapper.insert(userEntity);
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
