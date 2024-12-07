package com.example.sms.service.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.service.system.user.UserRepository;
import com.github.pagehelper.PageInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        ApplicationExecutionHistory newApplicationExecutionHistory = ApplicationExecutionHistory.of(applicationExecutionHistory.getId(), applicationExecutionHistory.getProcessName(), applicationExecutionHistory.getProcessCode(), applicationExecutionHistory.getProcessType(), applicationExecutionHistory.getProcessStart(), applicationExecutionHistory.getProcessEnd(), applicationExecutionHistory.getProcessFlag(), applicationExecutionHistory.getProcessDetails(), user);
        auditRepository.save(newApplicationExecutionHistory);
    }

    /**
     * アプリケーション実行履歴削除
     */
    public void delete(Integer id) {
        auditRepository.deleteById(id);
    }
}
