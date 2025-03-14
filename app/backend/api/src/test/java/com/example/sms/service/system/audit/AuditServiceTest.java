package com.example.sms.service.system.audit;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.service.system.auth.AuthApiService;
import com.example.sms.service.system.user.UserManagementService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

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
        @DisplayName("アプリケーション実行履歴一覧を取得できる")
        void getApplicationExecutionHistoryList() {
            ApplicationExecutionHistoryList result = auditService.selectAll();
            assertEquals(2, result.asList().size());
        }

        @Test
        @DisplayName("アプリケーション実行履歴を登録できる")
        void registerApplicationExecutionHistory() {
            ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, "その他", "9999", ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", null);
            UserId userId = UserId.of("U777777");
            auditService.register(applicationExecutionHistory, userId);

            ApplicationExecutionHistoryList result = auditService.selectAll();

            assertEquals(3, result.asList().size());
            assertEquals(userId, result.asList().get(2).getUser().getUserId());
        }

        @Test
        @DisplayName("アプリケーション実行履歴を取得できる")
        void getApplicationExecutionHistory() {
            UserId userId = UserId.of("U777777");
            User user = User.of(userId.getValue(), "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
            userManagementService.register(user);
            ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(1, "その他", "9999", ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", user);
            auditService.register(applicationExecutionHistory, userId);

            ApplicationExecutionHistory result = auditService.find(String.valueOf(1));

            assertEquals(applicationExecutionHistory, result);
        }

        @Test
        @DisplayName("アプリケーション実行履歴をページングで取得できる")
        void getApplicationExecutionHistoryWithPageInfo() {
            assertEquals(2, auditService.selectAllWithPageInfo().getSize());
        }

        @Nested
        @DisplayName("アプリケーション実行履歴を検索できる")
        class SearchApplicationExecutionHistoryTests {
            @Test
            @DisplayName("３条件が全て一致するアプリケーション実行履歴を検索できる")
            void case1() {
                ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, ApplicationExecutionProcessType.ユーザー登録.getName(), ApplicationExecutionProcessType.ユーザー登録.getCode(), ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", null);
                UserId userId = UserId.of("U777777");
                auditService.register(applicationExecutionHistory, userId);
                AuditCriteria criteria = AuditCriteria.of(ApplicationExecutionProcessType.ユーザー登録, ApplicationExecutionHistoryType.同期, ApplicationExecutionProcessFlag.未実行);

                PageInfo<ApplicationExecutionHistory> result = auditService.searchWithPageInfo(criteria);

                assertEquals(1, result.getSize());
                assertEquals(ApplicationExecutionProcessType.ユーザー登録, result.getList().get(0).getProcess().getProcessType());
                assertEquals(ApplicationExecutionProcessFlag.未実行, result.getList().get(0).getProcessFlag());
                assertEquals(ApplicationExecutionHistoryType.同期, result.getList().get(0).getType());
            }
            @Test
            @DisplayName("２条件が一致するアプリケーション実行履歴を検索できる")
            void case2() {
                ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, ApplicationExecutionProcessType.ユーザー登録.getName(), ApplicationExecutionProcessType.ユーザー登録.getCode(), ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", null);
                UserId userId = UserId.of("U777777");
                auditService.register(applicationExecutionHistory, userId);
                AuditCriteria criteria = AuditCriteria.of(ApplicationExecutionProcessType.ユーザー登録, ApplicationExecutionHistoryType.同期, null);

                PageInfo<ApplicationExecutionHistory> result = auditService.searchWithPageInfo(criteria);

                assertEquals(1, result.getSize());
                assertEquals(ApplicationExecutionProcessType.ユーザー登録, result.getList().get(0).getProcess().getProcessType());
                assertEquals(ApplicationExecutionHistoryType.同期, result.getList().get(0).getType());
            }
            @Test
            @DisplayName("１条件が一致するアプリケーション実行履歴を検索できる")
            void case3() {
                ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, ApplicationExecutionProcessType.ユーザー登録.getName(), ApplicationExecutionProcessType.ユーザー登録.getCode(), ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", null);
                UserId userId = UserId.of("U777777");
                auditService.register(applicationExecutionHistory, userId);
                AuditCriteria criteria = AuditCriteria.of(ApplicationExecutionProcessType.ユーザー登録, null, null);

                PageInfo<ApplicationExecutionHistory> result = auditService.searchWithPageInfo(criteria);

                assertEquals(1, result.getSize());
                assertEquals(ApplicationExecutionProcessType.ユーザー登録, result.getList().get(0).getProcess().getProcessType());
            }
            @Test
            @DisplayName("条件なしの場合はアプリケーション実行履歴を全件検索できる")
            void case4() {
                ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, ApplicationExecutionProcessType.ユーザー登録.getName(), ApplicationExecutionProcessType.ユーザー登録.getCode(), ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", null);
                UserId userId = UserId.of("U777777");
                auditService.register(applicationExecutionHistory, userId);
                AuditCriteria criteria = AuditCriteria.of(null, null, null);

                PageInfo<ApplicationExecutionHistory> result = auditService.searchWithPageInfo(criteria);

                assertEquals(3, result.getSize());
            }
            @Test
            @DisplayName("条件に一致するアプリケーション実行履歴が存在しない場合は空のリストを返す")
            void case5() {
                ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, ApplicationExecutionProcessType.ユーザー登録.getName(), ApplicationExecutionProcessType.ユーザー登録.getCode(), ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", null);
                UserId userId = UserId.of("U777777");
                auditService.register(applicationExecutionHistory, userId);
                AuditCriteria criteria = AuditCriteria.of(ApplicationExecutionProcessType.商品登録, ApplicationExecutionHistoryType.非同期, ApplicationExecutionProcessFlag.実行済);

                PageInfo<ApplicationExecutionHistory> result = auditService.searchWithPageInfo(criteria);

                assertEquals(0, result.getSize());
            }
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

        @Test
        @DisplayName("アプリケーション実行履歴の記録を開始できる")
        void startApplicationExecutionHistory() {
            try (MockedStatic<AuthApiService> authApiServiceMockedStatic = mockStatic(AuthApiService.class)) {
                authApiServiceMockedStatic.when(AuthApiService::getCurrentUserId).thenReturn(UserId.of("U777777"));

                ApplicationExecutionHistory result = auditService.start(ApplicationExecutionProcessType.その他, ApplicationExecutionHistoryType.同期);

                assertNotNull(result.getId());
                assertEquals(ApplicationExecutionProcessFlag.実行中, result.getProcessFlag());
            }
        }

        @Test
        @DisplayName("アプリケーション実行履歴の記録を終了できる")
        void endApplicationExecutionHistory() {
            try (MockedStatic<AuthApiService> authApiServiceMockedStatic = mockStatic(AuthApiService.class)) {
                authApiServiceMockedStatic.when(AuthApiService::getCurrentUserId).thenReturn(UserId.of("U777777"));
                ApplicationExecutionHistory history = auditService.start(ApplicationExecutionProcessType.その他, ApplicationExecutionHistoryType.同期);

                auditService.end(history);

                ApplicationExecutionHistory result = auditService.find(String.valueOf(history.getId()));
                assertNotNull(result.getProcessEnd());
                assertEquals(ApplicationExecutionProcessFlag.実行済, result.getProcessFlag());
            }
        }

        @Test
        @DisplayName("アプリケーション実行履歴の記録をエラーで終了できる")
        void errorApplicationExecutionHistory() {
            try (MockedStatic<AuthApiService> authApiServiceMockedStatic = mockStatic(AuthApiService.class)) {
                authApiServiceMockedStatic.when(AuthApiService::getCurrentUserId).thenReturn(UserId.of("U777777"));
                ApplicationExecutionHistory history = auditService.start(ApplicationExecutionProcessType.その他, ApplicationExecutionHistoryType.同期);

                auditService.error(history, "error message");

                ApplicationExecutionHistory result = auditService.find(String.valueOf(history.getId()));
                assertNotNull(result.getProcessEnd());
                assertEquals(ApplicationExecutionProcessFlag.エラー, result.getProcessFlag());
                assertEquals("error message", result.getProcessDetails());
            }
        }

    }
}
