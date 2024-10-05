package com.example.sms;

import com.example.sms.domain.model.system.user.User;

public interface TestDataFactory {
    void setUpForAuthApiService();

    User User();
}
