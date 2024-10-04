package com.example.sms.infrastructure.datasource;

import com.example.sms.domain.model.RoleName;
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
public class UserJpaEntity implements Serializable {
    @Id
    private String userId;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
