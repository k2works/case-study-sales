package com.example.sms.infrastructure.datasource.system.user;

import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.datasource.autogen.model.Usr;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public User mapToDomainEntity(Usr userEntity) {
        return User.of(
                userEntity.getUserId(),
                userEntity.getPassword(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                RoleName.valueOf(userEntity.getRoleName()));
    }
    Usr mapToEntity(User user) {
        Usr usr = new Usr();
        usr.setUserId(user.getUserId().Value());
        usr.setPassword(user.getPassword().Value());
        usr.setFirstName(user.getName().FirstName());
        usr.setLastName(user.getName().LastName());
        usr.setRoleName(user.getRoleName().name());
        return usr;
    }
}
