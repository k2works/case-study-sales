package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.master.employee.EmployeeResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.master.department.DepartmentService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部門API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department", description = "部門")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentApiController {
    final DepartmentService departmentManagementService;

    final Message message;

    public DepartmentApiController(DepartmentService departmentManagementService, Message message) {
        this.departmentManagementService = departmentManagementService;
        this.message = message;
    }

    @Operation(summary = "部門一覧を取得する", description = "部門一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Department> result = departmentManagementService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を取得する", description = "部門を取得する")
    @GetMapping("/{departmentCode}/{departmentStartDate}")
    public ResponseEntity<?> find(@PathVariable String departmentCode, @PathVariable String departmentStartDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            DepartmentId departmentId = DepartmentId.of(departmentCode, LocalDateTime.parse(departmentStartDate, formatter));
            DepartmentList department = departmentManagementService.findByCode(departmentId);
            return ResponseEntity.ok(department.asList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を登録する", description = "部門を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.部門登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated DepartmentResource resource) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            Department department = Department.of(
                    DepartmentId.of(resource.getDepartmentCode(), LocalDateTime.parse(resource.getStartDate(), formatter)),
                    LocalDateTime.parse(resource.getEndDate(), formatter),
                    resource.getDepartmentName(),
                    Integer.parseInt(resource.getLayer()),
                    resource.getPath(),
                    resource.getLowerType().getValue(),
                    resource.getSlitYn().getValue()
            );
            Department departmentOptional = departmentManagementService.find(department.getDepartmentId());
            if (departmentOptional != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.department.already.exist")));
            }
            departmentManagementService.register(department);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.department.registered")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を更新する", description = "部門を更新する")
    @PutMapping("/{departmentCode}/{departmentStartDate}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.部門更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String departmentCode, @PathVariable String departmentStartDate, @RequestBody DepartmentResource departmentResource) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            DepartmentId departmentId = DepartmentId.of(departmentCode, LocalDateTime.parse(departmentStartDate, formatter));
            Department department = Department.of(
                    departmentId,
                    LocalDateTime.parse(departmentResource.getEndDate(), formatter),
                    departmentResource.getDepartmentName(),
                    Integer.parseInt(departmentResource.getLayer()),
                    departmentResource.getPath(),
                    departmentResource.getLowerType().getValue(),
                    departmentResource.getSlitYn().getValue()
            );
            List<Employee> addEmployees = getAddFilteredEmployees(departmentResource);
            List<Employee> deleteEmployees = getDeleteFilteredEmployees(departmentResource);

            departmentManagementService.save(department, addEmployees, deleteEmployees);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.department.updated")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を削除する", description = "部門を削除する")
    @DeleteMapping("/{departmentCode}/{departmentStartDate}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.部門削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String departmentCode, @PathVariable String departmentStartDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            DepartmentId departmentId = DepartmentId.of(departmentCode, LocalDateTime.parse(departmentStartDate, formatter));
            Department department = departmentManagementService.find(departmentId);
            if (department == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.department.not.exist")));
            }
            departmentManagementService.delete(departmentId);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.department.deleted")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private static List<Employee> getAddFilteredEmployees(DepartmentResource departmentResource) {
        return departmentResource.getEmployees() == null ? Collections.emptyList() :
                departmentResource.getEmployees().stream()
                        .filter(EmployeeResource::isAddFlag)
                        .map(employeeResource -> Employee.of(
                                employeeResource.getEmpCode(),
                                employeeResource.getEmpName(),
                                employeeResource.getEmpNameKana(),
                                employeeResource.getTel(),
                                employeeResource.getFax(),
                                employeeResource.getOccuCode()
                        ))
                        .collect(Collectors.toList());
    }

    private static List<Employee> getDeleteFilteredEmployees(DepartmentResource departmentResource) {
        return departmentResource.getEmployees() == null ? Collections.emptyList() :
                departmentResource.getEmployees().stream()
                        .filter(EmployeeResource::isDeleteFlag)
                        .map(employeeResource -> Employee.of(
                                employeeResource.getEmpCode(),
                                employeeResource.getEmpName(),
                                employeeResource.getEmpNameKana(),
                                employeeResource.getTel(),
                                employeeResource.getFax(),
                                employeeResource.getOccuCode()
                        ))
                        .collect(Collectors.toList());
    }
}

