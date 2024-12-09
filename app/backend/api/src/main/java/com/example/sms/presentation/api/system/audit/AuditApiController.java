package com.example.sms.presentation.api.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.system.audit.AuditService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 監査API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit", description = "監査")
public class AuditApiController {
    final AuditService auditService;

    final Message message;

    public AuditApiController(AuditService auditService, Message message) {
        this.auditService = auditService;
        this.message = message;
    }

    @Operation(summary = "アプリケーション実行履歴一覧を取得する", description = "アプリケーション実行履歴一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<ApplicationExecutionHistory> result = auditService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "アプリケーション実行履歴を取得する", description = "アプリケーション実行履歴を取得する")
    @GetMapping("/{applicationExecutionHistoryId}")
    public ResponseEntity<?> find(@PathVariable String applicationExecutionHistoryId) {
        try {
            ApplicationExecutionHistory applicationExecutionHistory = auditService.find(applicationExecutionHistoryId);
            return ResponseEntity.ok(applicationExecutionHistory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "アプリケーション実行履歴を削除する", description = "アプリケーション実行履歴を削除する")
    @DeleteMapping("/{applicationExecutionHistoryId}")
    public ResponseEntity<?> delete(@PathVariable String applicationExecutionHistoryId) {
        try {
            auditService.delete(Integer.valueOf(applicationExecutionHistoryId));
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.audit.history.deleted")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}

