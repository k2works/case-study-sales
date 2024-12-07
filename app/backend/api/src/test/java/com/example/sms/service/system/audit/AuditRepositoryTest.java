package com.example.sms.service.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("監査レポジトリ")
public class AuditRepositoryTest {

    @Autowired
    private AuditRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private static ApplicationExecutionHistory getHistory() {
        return ApplicationExecutionHistory.of(1, "processName", "processCode", ApplicationExecutionHistoryType.同期処理, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), 1,  "processDetails");
    }

    @Test
    @DisplayName("アプリケーション実行履歴一覧を取得できる")
    void shouldRetrieveAllAuditLogs() {
        ApplicationExecutionHistory history = getHistory();

        repository.save(history);

        ApplicationExecutionHistoryList actual = repository.selectAll();
        assertEquals(1, actual.size());
    }
}
