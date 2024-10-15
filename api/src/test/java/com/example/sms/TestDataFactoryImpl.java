package com.example.sms;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestDataFactoryImpl implements TestDataFactory {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public void setUpForAuthApiService() {
        userRepository.deleteAll();
        userRepository.save(user());
        userRepository.save(admin());
    }

    @Override
    public User User() {
        return user();
    }

    @Override
    public Department Department() {
        return department("30000", "部門3");
    }

    @Override
    public void setUpForUserManagementService() {
        userRepository.deleteAll();
        userRepository.save(user());
        userRepository.save(admin());
    }

    @Override
    public void setUpForDepartmentService() {
        departmentRepository.deleteAll();
        departmentRepository.save(department("10000", "部門1"));
        departmentRepository.save(department("20000", "部門2"));
    }

    private static User user() {
        return User.of("U999999", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }

    private static User admin() {
        return User.of("U888888", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }

    private static Department department(String departmentId, String departmentName) {
        return Department.of(DepartmentId.of(departmentId, LocalDateTime.of(2021, 1, 1, 0, 0, 0)), LocalDateTime.of(9999, 12, 31, 0, 0), departmentName, 0, departmentId + "~", 0, 1);
    }
}
