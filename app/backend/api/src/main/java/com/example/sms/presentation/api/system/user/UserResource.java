package com.example.sms.presentation.api.system.user;

import com.example.sms.domain.type.user.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "ユーザー")
public class UserResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String userId;
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private RoleName roleName;

}
