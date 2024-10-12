package com.example.sms.service.master.department;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("部門レポジトリ")
public class DepartmentRepositoryTest {
    @Autowired
    private DepartmentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("部門一覧を取得できる")
    void shouldRetrieveAllDepartments() {

    }
}
