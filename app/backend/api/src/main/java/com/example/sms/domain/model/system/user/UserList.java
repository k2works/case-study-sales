package com.example.sms.domain.model.system.user;


import java.util.List;

/**
 * ユーザー一覧
 */
public class UserList {
    List<User> value;

    public UserList(List<User> value) {
        this.value = value;
    }

    public int size() {
        return value.size();
    }

    public List<User> asList() {
        return value;
    }
}
