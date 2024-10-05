package com.example.sms.presentation.api.system.auth;

import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.infrastructure.repository.system.user.UserRepository;
import com.example.sms.infrastructure.security.JWTAuth.JwtUtils;
import com.example.sms.infrastructure.security.JWTAuth.payload.request.LoginRequest;
import com.example.sms.infrastructure.security.JWTAuth.payload.request.SignupRequest;
import com.example.sms.infrastructure.security.JWTAuth.payload.response.JwtResponse;
import com.example.sms.infrastructure.security.JWTAuth.payload.response.MessageResponse;
import com.example.sms.service.system.auth.AuthApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 認証API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "JWTAuth", description = "JWT認証")
public class AuthApiController {
    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    final AuthApiService authApiService;

    public AuthApiController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthApiService authApiService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authApiService = authApiService;
    }

    @Operation(summary = "ユーザー認証", description = "データベースに登録されているユーザーを認証する")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> user = userRepository.findById(loginRequest.getUserId());
            if (((Optional<?>) user).isEmpty()) {
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
            Optional<User> user = userRepository.findById(signupRequest.getUserId());
            if (user.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: UserId is already taken"));
            }

            UserId userId = new UserId(signupRequest.getUserId());
            String password = passwordEncoder.encode(signupRequest.getPassword());
            String userNameFirst = signupRequest.getFirstName();
            String userNameLast = signupRequest.getLastName();
            String role = signupRequest.getRole();
            RoleName roleName;
            if (role == null) {
                roleName = RoleName.USER;
            } else {
                roleName = RoleName.valueOf(role);
            }
            User newUser = new User(userId, password, userNameFirst, userNameLast, roleName);
            userRepository.save(newUser);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
