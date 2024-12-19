package com.example.sms.domain.model.system.user;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ユーザー一覧
 */
public class UserList {
    List<User> value;

    public UserList(List<User> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public UserList add(User user) {
        List<User> newValue = new ArrayList<>(value);
        newValue.add(user);
        return new UserList(newValue);
    }

    public List<User> asList() {
        return value;
    }
}
