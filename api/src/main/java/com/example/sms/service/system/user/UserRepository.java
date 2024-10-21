package com.example.sms.service.system.user;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserList;
import com.example.sms.infrastructure.datasource.system.user.Usr;

import java.util.List;
import java.util.Optional;

/**
 * ユーザーレポジトリ
 */
public interface UserRepository {
    Optional<User> findById(String userId);

    UserList selectAll();

    List<Usr> selectAllWithPageNation();

    void save(User user);

    void update(User user);

    void deleteById(String userId);

    void deleteAll();

}
