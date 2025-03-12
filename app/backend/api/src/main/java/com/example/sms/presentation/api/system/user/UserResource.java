package com.example.sms.presentation.api.system.user;

import com.example.sms.domain.model.system.user.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "ユーザー")
public class UserResource {
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
