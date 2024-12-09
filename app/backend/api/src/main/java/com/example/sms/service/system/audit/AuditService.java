package com.example.sms.service.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessType;
import com.example.sms.service.system.auth.AuthApiService;
import com.example.sms.service.system.user.UserRepository;
import com.github.pagehelper.PageInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 監査サービス
 */
@Service
@Transactional
public class AuditService {
    final AuditRepository auditRepository;
    final UserRepository userRepository;

    public AuditService(AuditRepository auditRepository, UserRepository userRepository) {
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
    }

    /**
     * アプリケーション実行履歴一覧
     */
    public ApplicationExecutionHistoryList selectAll() {
        return auditRepository.selectAll();
    }

    /**
     * アプリケーション実行履歴一覧（ページング）
     */
    public PageInfo<ApplicationExecutionHistory> selectAllWithPageInfo() {
        return auditRepository.selectAllWithPageInfo();
    }

    /**
     * アプリケーション実行履歴新規登録
     */
    public void register(ApplicationExecutionHistory applicationExecutionHistory, UserId userId) {
        User user = userRepository.findById(userId.Value()).orElse(null);
        ApplicationExecutionHistory newApplicationExecutionHistory = ApplicationExecutionHistory.of(applicationExecutionHistory.getId(), applicationExecutionHistory.getProcess().getName(), applicationExecutionHistory.getProcess().getCode(), applicationExecutionHistory.getType(), applicationExecutionHistory.getProcessStart(), applicationExecutionHistory.getProcessEnd(), applicationExecutionHistory.getProcessFlag(), applicationExecutionHistory.getProcessDetails(), user);
        auditRepository.save(newApplicationExecutionHistory);
    }

    /**
     * アプリケーション実行履歴削除
     */
    public void delete(Integer id) {
        auditRepository.deleteById(id);
    }

    /**
     * アプリケーション実行履歴取得
     */
    public ApplicationExecutionHistory find(String applicationExecutionHistoryId) {
        return auditRepository.findById(Integer.valueOf(applicationExecutionHistoryId)).orElse(null);
    }

    /**
     * アプリケーション実行履歴開始
     */
    public ApplicationExecutionHistory start(ApplicationExecutionProcessType process) {
        String userId = AuthApiService.getCurrentUserId().Value();
        User user = userRepository.findById(userId).orElse(null);
        LocalDateTime processStart = LocalDateTime.now();
        ApplicationExecutionHistory history = ApplicationExecutionHistory.of(null, process.getName(), process.getCode(), ApplicationExecutionHistoryType.SYNC, processStart, null, ApplicationExecutionProcessFlag.START, null, user);
        return auditRepository.start(history);
    }

    /**
     * アプリケーション実行履歴終了
     */
    public void end(ApplicationExecutionHistory history) {
        LocalDateTime processEnd = LocalDateTime.now();
        ApplicationExecutionHistory startHistory = find(String.valueOf(history.getId()));
        ApplicationExecutionHistory endHistory = ApplicationExecutionHistory.of(history.getId(), startHistory.getProcess().getName(), startHistory.getProcess().getCode(), startHistory.getType(), startHistory.getProcessStart(), processEnd, ApplicationExecutionProcessFlag.END, startHistory.getProcessDetails(), startHistory.getUser());
        auditRepository.save(endHistory);
    }

    /**
     * アプリケーション実行履歴エラー
     */
    public void error(ApplicationExecutionHistory history, String message) {
        LocalDateTime processEnd = LocalDateTime.now();
        ApplicationExecutionHistory startHistory = find(String.valueOf(history.getId()));
        ApplicationExecutionHistory endHistory = ApplicationExecutionHistory.of(history.getId(), startHistory.getProcess().getName(), startHistory.getProcess().getCode(), startHistory.getType(), startHistory.getProcessStart(), processEnd, ApplicationExecutionProcessFlag.ERROR, message, startHistory.getUser());
        auditRepository.save(endHistory);
    }
}
