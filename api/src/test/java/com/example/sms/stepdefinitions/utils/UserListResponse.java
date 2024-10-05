package com.example.sms.stepdefinitions.utils;

import com.example.sms.domain.model.system.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class UserListResponse implements Serializable {
    @JsonProperty
    private List<User> users;
}
