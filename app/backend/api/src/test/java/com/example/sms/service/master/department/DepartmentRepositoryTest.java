package com.example.sms.service.master.department;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("部門レポジトリ")
class DepartmentRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("root")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired
    private DepartmentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Department getDepartment() {
        return TestDataFactoryImpl.getDepartment("10000", LocalDateTime.of(2021, 1, 1, 0, 0), "全社");
    }

    @Test
    @DisplayName("部門一覧を取得できる")
    void shouldRetrieveAllDepartments() {
        Department department = getDepartment();

        repository.save(department);

        assertEquals(1, repository.selectAll().size());
    }

    @Test
    @DisplayName("部門を登録できる")
    void shouldRegisterADepartment() {
        Department department = getDepartment();

        repository.save(department);

        Department actual = repository.findById(department.getDepartmentId()).get();
        assertEquals(department.getDepartmentId(), actual.getDepartmentId());
        assertEquals(department.getEndDate(), actual.getEndDate());
        assertEquals(department.getDepartmentName(), actual.getDepartmentName());
        assertEquals(department.getLayer(), actual.getLayer());
        assertEquals(department.getPath(), actual.getPath());
        assertEquals(department.getLowerType(), actual.getLowerType());
        assertEquals(department.getSlitYn(), actual.getSlitYn());
    }

    @Test
    @DisplayName("部門を更新できる")
    void shouldUpdateADepartment() {
        Department department = getDepartment();
        repository.save(department);

        Department updateDepartment = Department.of(DepartmentId.of("10000", LocalDateTime.of(2021, 1, 1, 0, 0, 0)), LocalDateTime.of(9999, 12, 31, 0, 0), "全社2", 1, "10000~", 1, 0);
        repository.save(updateDepartment);

        Department actual = repository.findById(updateDepartment.getDepartmentId()).get();
        assertEquals(updateDepartment.getDepartmentId(), actual.getDepartmentId());
        assertEquals(updateDepartment.getEndDate(), actual.getEndDate());
        assertEquals(updateDepartment.getDepartmentName(), actual.getDepartmentName());
        assertEquals(updateDepartment.getLayer(), actual.getLayer());
        assertEquals(updateDepartment.getPath(), actual.getPath());
        assertEquals(updateDepartment.getLowerType(), actual.getLowerType());
        assertEquals(updateDepartment.getSlitYn(), actual.getSlitYn());
    }

    @Test
    @DisplayName("部門を削除できる")
    void shouldDeleteADepartment() {
        Department department = getDepartment();
        repository.save(department);

        repository.deleteById(department.getDepartmentId());

        assertEquals(0, repository.selectAll().size());
    }
}
