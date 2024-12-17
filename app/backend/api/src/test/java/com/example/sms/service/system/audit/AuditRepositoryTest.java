package com.example.sms.service.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.type.user.RoleName;
import com.example.sms.service.system.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("監査レポジトリ")
public class AuditRepositoryTest {

    @Autowired
    private AuditRepository repository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        repository.deleteAll();
        userRepository.save(getUser());
    }

    private static User getUser() {
        return User.of("U999999", "a234567Z", "firstName", "lastName", RoleName.USER);
    }

    private static ApplicationExecutionHistory getHistory() {
        return ApplicationExecutionHistory.of(1, "その他", "9999", ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.NOT_EXECUTED,  "processDetails", getUser());
    }

    @Test
    @DisplayName("アプリケーション実行履歴一覧を取得できる")
    void shouldRetrieveAllAuditLogs() {
        ApplicationExecutionHistory history = getHistory();

        repository.save(history);

        ApplicationExecutionHistoryList actual = repository.selectAll();
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("アプリケーション実行履歴を登録できる")
    void shouldRetrieveAuditLog() {
        ApplicationExecutionHistory history = getHistory();

        repository.save(history);

        ApplicationExecutionHistory actual = repository.findById(history.getId()).get();
        assertEquals(history.getId(), actual.getId());
        assertEquals(history.getProcess(), actual.getProcess());
        assertEquals(history.getType(), actual.getType());
        assertEquals(history.getProcessStart(), actual.getProcessStart());
        assertEquals(history.getProcessEnd(), actual.getProcessEnd());
        assertEquals(history.getProcessFlag(), actual.getProcessFlag());
        assertEquals(getUser(), actual.getUser());
    }

    @Test
    @DisplayName("アプリケーション実行履歴を削除できる")
    void shouldDeleteAuditLog() {
        ApplicationExecutionHistory history = getHistory();
        repository.save(history);

        repository.deleteById(history.getId());

        Optional<ApplicationExecutionHistory> actual = repository.findById(history.getId());
        assertEquals(Optional.empty(), actual);
    }
}
