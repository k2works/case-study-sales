package com.example.sms.presentation.api.system.download;

import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.example.sms.service.system.download.DownloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStreamWriter;

/**
 * データダウンロードAPI
 */
@Slf4j
@RestController
@RequestMapping("/api/downloads")
@Tag(name = "Download", description = "データダウンロード")
public class DownloadApiController {
    final DownloadService downloadService;
    final MessageSource messageSource;

    public DownloadApiController(DownloadService downloadService, MessageSource messageSource) {
        this.downloadService = downloadService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "ダウンロード件数", description = "ダウンロード件数を取得する")
    @PostMapping("/count")
    public ResponseEntity<?> count(@RequestBody DownloadCriteriaResource resource) {
        try {
            DownloadCriteria condition = DownloadCriteriaResource.of(resource.getTarget());
            return ResponseEntity.ok(downloadService.count(condition));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "ダウンロード", description = "ダウンロードする")
    @PostMapping("/download")
    @AuditAnnotation(process = ApplicationExecutionProcessType.データダウンロード, type = ApplicationExecutionHistoryType.同期)
    public void download(@RequestBody DownloadCriteriaResource resource, HttpServletResponse response) {
        DownloadCriteria condition = DownloadCriteriaResource.of(resource.getTarget());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + condition.getFileName());
        try (OutputStreamWriter streamWriter = new OutputStreamWriter(response.getOutputStream(), "Windows-31J")) {
            downloadService.download(streamWriter, condition);
        } catch (Exception e) {
            log.error("ダウンロードエラー", e);
        }
    }
}
