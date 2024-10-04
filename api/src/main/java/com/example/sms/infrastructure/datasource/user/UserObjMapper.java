package com.example.sms.infrastructure.datasource.user;

import com.example.sms.domain.model.RoleName;
import com.example.sms.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserObjMapper {
    User mapToDomainEntity(Usr userEntity) {
        return new User(
                userEntity.getUserId(),
                userEntity.getPassword(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                RoleName.valueOf(userEntity.getRoleName()));
    }
    Usr mapToEntity(User user) {
        Usr usr = new Usr();
        usr.setUserId(user.getUserId());
        usr.setPassword(user.getPassword());
        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setRoleName(user.getRoleName().name());
        return usr;
    }
}
