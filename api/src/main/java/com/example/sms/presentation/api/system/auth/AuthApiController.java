package com.example.sms.presentation.api.system.auth;

import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.infrastructure.security.JWTAuth.payload.request.LoginRequest;
import com.example.sms.infrastructure.security.JWTAuth.payload.request.SignupRequest;
import com.example.sms.infrastructure.security.JWTAuth.payload.response.JwtResponse;
import com.example.sms.infrastructure.security.JWTAuth.payload.response.MessageResponse;
import com.example.sms.service.system.auth.AuthApiService;
import com.example.sms.service.system.user.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 認証API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "JWTAuth", description = "JWT認証")
public class AuthApiController {
    final PasswordEncoder passwordEncoder;
    final UserManagementService userManagementService;
    final AuthApiService authApiService;

    public AuthApiController(PasswordEncoder passwordEncoder, UserManagementService userManagementService, AuthApiService authApiService) {
        this.passwordEncoder = passwordEncoder;
        this.userManagementService = userManagementService;
        this.authApiService = authApiService;
    }

    @Operation(summary = "ユーザー認証", description = "データベースに登録されているユーザーを認証する")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = userManagementService.find(new UserId(loginRequest.getUserId()));
            if (user == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not exist"));
            }

            JwtResponse jwtResponse = authApiService.authenticateUser(loginRequest.getUserId(), loginRequest.getPassword());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "ユーザー登録", description = "ユーザーを新規登録する")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            UserId userId = new UserId(signupRequest.getUserId());
            String password = passwordEncoder.encode(signupRequest.getPassword());
            User userOptional = userManagementService.find(userId);
            if (userOptional != null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: UserId is already taken"));
            }
            String role = signupRequest.getRole();
            RoleName roleName;
            if (role == null) {
                roleName = RoleName.USER;
            } else {
                roleName = RoleName.valueOf(role);
            }
            User user = User.of(userId.Value(), password, signupRequest.getFirstName(), signupRequest.getLastName(), roleName);
            userManagementService.register(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
