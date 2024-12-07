package com.example.sms.service.system.audit;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.service.system.user.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
@DisplayName("監査サービス")
public class AuditServiceTest {
    @Autowired
    AuditService auditService;
    @Autowired
    UserManagementService userManagementService;

    @Autowired
    TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForAuditService();
    }

    @Nested
    @DisplayName("アプリケーション実行履歴")
    class ApplicationExecutionHistoryTests {
        @Test
        @DisplayName("アプリケーション実行履歴を取得できる")
        void getApplicationExecutionHistory() {
            ApplicationExecutionHistoryList result = auditService.selectAll();
            assertEquals(2, result.asList().size());
        }

        @Test
        @DisplayName("アプリケーション実行履歴を登録できる")
        void registerApplicationExecutionHistory() {
            ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, "processName", "processCode", ApplicationExecutionHistoryType.同期処理, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), 1,  "processDetails", null);
            UserId userId = UserId.of("U777777");
            auditService.register(applicationExecutionHistory, userId);
            ApplicationExecutionHistoryList result = auditService.selectAll();
            assertEquals(3, result.asList().size());
            assertEquals(userId, result.asList().get(2).getUser().getUserId());
        }

        @Test
        @DisplayName("アプリケーション実行履歴をページングで取得できる")
        void getApplicationExecutionHistoryWithPageInfo() {
            assertEquals(2, auditService.selectAllWithPageInfo().getSize());
        }

        @Test
        @DisplayName("実行ユーザーを削除した場合は、アプリケーション実行履歴の実行ユーザーはnullになる")
        void deleteExecutionUser() {
            UserId userId = UserId.of("U777777");
            userManagementService.delete(userId);

            ApplicationExecutionHistoryList result = auditService.selectAll();
            boolean isUserDeleted = result.asList().stream().anyMatch(a -> a.getUser() == null);
            assertTrue(isUserDeleted);
        }
    }
}
