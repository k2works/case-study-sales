package com.example.sms.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ユーザー
 */
@Entity
@Table(name = "usr", schema = "system")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    private String userId;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
