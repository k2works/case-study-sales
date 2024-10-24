package com.example.sms.service.system.user;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.domain.model.system.user.UserList;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ユーザー管理サービス
 */
@Service
@Transactional
public class UserManagementService {
    final UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * ユーザー一覧
     */
    public UserList selectAll() {
        return userRepository.selectAll();
    }

    /**
     * ユーザー一覧（ページング）
     */
    public PageInfo<User> selectAllWithPageInfo() {
        return userRepository.selectAllWithPageInfo();
    }
    /**
     * ユーザー新規登録
     */
    public void register(User user) {
        userRepository.save(user);
    }

    /**
     * ユーザー情報編集
     */
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * ユーザー削除
     */
    public void delete(UserId userId) {
        userRepository.deleteById(userId.Value());
    }

    /**
     * ユーザー検索
     */
    public User find(UserId userId) {
        return userRepository.findById(userId.Value()).orElse(null);
    }

}
