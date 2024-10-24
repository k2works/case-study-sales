package com.example.sms.service.system.user;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

/**
 * ユーザーレポジトリ
 */
public interface UserRepository {
    Optional<User> findById(String userId);

    UserList selectAll();

    PageInfo<User> selectAllWithPageInfo();

    void save(User user);

    void deleteById(String userId);

    void deleteAll();

}
