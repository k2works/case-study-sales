package com.example.sms.service.system.user;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.repository.system.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ユーザー管理サービス
 */
@Service
public class UserManagementService {
    final UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * ユーザー一覧
     */
    public List<User> selectAll() {
        return userRepository.selectAll();
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
    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    /**
     * ユーザー検索
     */
    public User find(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
