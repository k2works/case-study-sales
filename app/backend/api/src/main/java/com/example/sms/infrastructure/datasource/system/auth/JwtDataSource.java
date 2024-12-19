package com.example.sms.infrastructure.datasource.system.auth;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.datasource.autogen.mapper.UsrMapper;
import com.example.sms.infrastructure.datasource.autogen.model.Usr;
import com.example.sms.infrastructure.datasource.system.user.UserEntityMapper;
import com.example.sms.service.system.auth.AuthRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JwtDataSource implements AuthRepository {
    final UsrMapper userMapper;
    final UserObjMapper userObjMapper;
    final JwtUtils jwtUtils;

    public JwtDataSource(UsrMapper userMapper, UserObjMapper userObjMapper, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.userObjMapper = userObjMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Optional<User> findById(String userId) {
        Optional<Usr> userEntity = Optional.ofNullable(userMapper.selectByPrimaryKey(userId));
        return userEntity.map(userEntityMapper::mapToDomainEntity);
    }

    @Override
    public String generateJwtToken(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }

    @Override
    public String getUserNameFromJwtToke(String authToken) {
        return jwtUtils.getUserNameFromJwtToke(authToken);
    }

    @Override
    public boolean validateJwtToke(String authToken) {
        return jwtUtils.validateJwtToke(authToken);
    }
}
