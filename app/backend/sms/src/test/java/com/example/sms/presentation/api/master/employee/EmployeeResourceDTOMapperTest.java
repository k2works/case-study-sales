package com.example.sms.presentation.api.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.service.master.employee.EmployeeCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("社員DTOマッパーテスト")
class EmployeeResourceDTOMapperTest {

    @Test
    @DisplayName("社員リソースを社員エンティティに変換する")
    void testConvertEntity_validResource_shouldReturnEmployee() {
        // Arrange
        String empCode = "EMP123";
        String empFirstName = "太郎";
        String empLastName = "山田";
        String tel = "03-1234-5678";
        String fax = "03-8765-4321";
        String occuCode = "OC001";

        EmployeeResource resource = EmployeeResource.builder()
                .empCode(empCode)
                .empFirstName(empFirstName)
                .empLastName(empLastName)
                .tel(tel)
                .fax(fax)
                .occuCode(occuCode)
                .build();

        Department department = mock(Department.class);
        User user = mock(User.class);

        // Act
        Employee employee = EmployeeResourceDTOMapper.convertEntity(resource, department, user);

        // Assert
        assertNotNull(employee);
        assertEquals(empCode, employee.getEmpCode().getValue());
        assertEquals(empFirstName + " " + empLastName, employee.getEmpName().Name());
        assertEquals(tel, employee.getTel().getValue());
        assertEquals(fax, employee.getFax().getValue());
        assertEquals(occuCode, employee.getOccuCode().getValue());
        assertSame(department, employee.getDepartment());
        assertSame(user, employee.getUser());
    }

    @Test
    @DisplayName("社員リソースにnull値がある場合でも変換できる")
    void testConvertEntity_nullValuesInResource_shouldHandleNulls() {
        // Arrange
        String empCode = "EMP123";
        String empFirstName = "太郎";
        String empLastName = "山田";

        EmployeeResource resource = EmployeeResource.builder()
                .empCode(empCode)
                .empFirstName(empFirstName)
                .empLastName(empLastName)
                .tel(null)
                .fax(null)
                .occuCode(null)
                .build();

        Department department = mock(Department.class);
        User user = mock(User.class);

        // Act
        Employee employee = EmployeeResourceDTOMapper.convertEntity(resource, department, user);

        // Assert
        assertNotNull(employee);
        assertEquals(empCode, employee.getEmpCode().getValue());
        assertEquals(empFirstName + " " + empLastName, employee.getEmpName().Name());
        assertNull(employee.getTel());
        assertNull(employee.getFax());
        assertNull(employee.getOccuCode());
        assertSame(department, employee.getDepartment());
        assertSame(user, employee.getUser());
    }

    @Test
    @DisplayName("社員検索条件リソースを社員検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnEmployeeCriteria() {
        // Arrange
        String employeeCode = "EMP123";
        String employeeFirstName = "太郎";
        String employeeLastName = "山田";
        String employeeFirstNameKana = "タロウ";
        String employeeLastNameKana = "ヤマダ";
        String phoneNumber = "03-1234-5678";
        String faxNumber = "03-8765-4321";
        String departmentCode = "D001";

        EmployeeCriteriaResource resource = new EmployeeCriteriaResource();
        resource.setEmployeeCode(employeeCode);
        resource.setEmployeeFirstName(employeeFirstName);
        resource.setEmployeeLastName(employeeLastName);
        resource.setEmployeeFirstNameKana(employeeFirstNameKana);
        resource.setEmployeeLastNameKana(employeeLastNameKana);
        resource.setPhoneNumber(phoneNumber);
        resource.setFaxNumber(faxNumber);
        resource.setDepartmentCode(departmentCode);

        // Act
        EmployeeCriteria criteria = EmployeeResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(employeeCode, criteria.getEmployeeCode());
        assertEquals(employeeFirstName, criteria.getEmployeeFirstName());
        assertEquals(employeeLastName, criteria.getEmployeeLastName());
        assertEquals(employeeFirstNameKana, criteria.getEmployeeFirstNameKana());
        assertEquals(employeeLastNameKana, criteria.getEmployeeLastNameKana());
        assertEquals(phoneNumber, criteria.getPhoneNumber());
        assertEquals(faxNumber, criteria.getFaxNumber());
        assertEquals(departmentCode, criteria.getDepartmentCode());
    }

    @Test
    @DisplayName("社員検索条件リソースにnull値がある場合でも変換できる")
    void testConvertToCriteria_nullValuesInResource_shouldHandleNulls() {
        // Arrange
        EmployeeCriteriaResource resource = new EmployeeCriteriaResource();
        resource.setEmployeeCode(null);
        resource.setEmployeeFirstName(null);
        resource.setEmployeeLastName(null);
        resource.setEmployeeFirstNameKana(null);
        resource.setEmployeeLastNameKana(null);
        resource.setPhoneNumber(null);
        resource.setFaxNumber(null);
        resource.setDepartmentCode(null);

        // Act
        EmployeeCriteria criteria = EmployeeResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertNull(criteria.getEmployeeCode());
        assertNull(criteria.getEmployeeFirstName());
        assertNull(criteria.getEmployeeLastName());
        assertNull(criteria.getEmployeeFirstNameKana());
        assertNull(criteria.getEmployeeLastNameKana());
        assertNull(criteria.getPhoneNumber());
        assertNull(criteria.getFaxNumber());
        assertNull(criteria.getDepartmentCode());
    }
}
