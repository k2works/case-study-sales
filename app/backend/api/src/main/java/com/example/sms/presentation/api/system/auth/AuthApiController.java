package com.example.sms.presentation.api.system.auth;

import com.example.sms.domain.model.system.auth.AuthUserDetails;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.presentation.api.system.auth.payload.request.LoginRequest;
import com.example.sms.presentation.api.system.auth.payload.request.SignupRequest;
import com.example.sms.presentation.api.system.auth.payload.response.JwtResponse;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.domain.BusinessException;
import com.example.sms.service.system.auth.AuthApiService;
import com.example.sms.service.system.user.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 認証API
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "JWTAuth", description = "JWT認証")
public class AuthApiController {
    final AuthenticationManager authenticationManager;
    final PasswordEncoder passwordEncoder;
    final UserManagementService userManagementService;
    final AuthApiService authApiService;

    public AuthApiController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserManagementService userManagementService, AuthApiService authApiService) {
        this.authenticationManager = authenticationManager;
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

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = authApiService.authenticateUser(authentication, loginRequest.getUserId(), loginRequest.getPassword());
            AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList();

            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUsername(), roles);
            return ResponseEntity.ok(jwtResponse);
        } catch (BusinessException e) {
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
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
