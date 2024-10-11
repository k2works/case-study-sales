package com.example.sms.presentation.api.system.user;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.domain.model.system.user.UserList;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.system.user.UserManagementService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * ユーザーAPI
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "ユーザー")
@PreAuthorize("hasRole('ADMIN')")
public class UserApiController {
    final PasswordEncoder passwordEncoder;

    final UserManagementService userManagementService;

    final Message message;

    public UserApiController(PasswordEncoder passwordEncoder, UserManagementService userManagementService, Message message) {
        this.passwordEncoder = passwordEncoder;
        this.userManagementService = userManagementService;
        this.message = message;
    }
    @Operation(summary = "ユーザー一覧を取得する", description = "ユーザー一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            UserList users = userManagementService.selectAll();
            return ResponseEntity.ok(new PageInfo<>(users.asList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "ユーザーを取得する", description = "ユーザーを取得する")
    @GetMapping("/{userId}")
    public ResponseEntity<?> find(@PathVariable String userId) {
        try {
            User user = userManagementService.find(new UserId(userId));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "ユーザーを登録する", description = "ユーザーを登録する")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated UserResource resource) {
        try {
            UserId userId = new UserId(resource.getUserId());
            String password = passwordEncoder.encode(resource.getPassword());
            User userOptional = userManagementService.find(userId);
            if (userOptional != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.user.already.exist")));
            }
            User user = User.of(userId.Value(), password, resource.getFirstName(), resource.getLastName(), resource.getRoleName());
            userManagementService.register(user);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.user.registered")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "ユーザーを更新する", description = "ユーザーを更新する")
    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable String userId, @RequestBody @Validated UserResource resource) {
        try {
            User userOptional = userManagementService.find(new UserId(userId));
            if (userOptional == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.user.not.exist")));
            }
            String password = passwordEncoder.encode(resource.getPassword());
            if (resource.getPassword().isEmpty()) {
                password = userOptional.getPassword().Value();
            }
            User user = User.of(userId, password, resource.getFirstName(), resource.getLastName(), resource.getRoleName());
            userManagementService.save(user);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.user.updated")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "ユーザーを削除する", description = "ユーザーを削除する")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId) {
        try {
            User userOptional = userManagementService.find(new UserId(userId));
            if (userOptional == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.user.not.exist")));
            }
            userManagementService.delete(new UserId(userId));
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.user.deleted")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
