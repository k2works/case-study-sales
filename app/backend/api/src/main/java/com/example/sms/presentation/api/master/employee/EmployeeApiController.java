package com.example.sms.presentation.api.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.department.DepartmentService;
import com.example.sms.service.master.employee.EmployeeCriteria;
import com.example.sms.service.master.employee.EmployeeService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.example.sms.service.system.user.UserManagementService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.sms.presentation.api.master.employee.EmployeeResourceDTOMapper.convertToCriteria;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 社員API
 */
@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee", description = "社員")
@PreAuthorize("hasRole('ADMIN')")
public class EmployeeApiController {
    final EmployeeService employeeManagementService;
    final DepartmentService departmentService;
    final UserManagementService userManagementService;
    final PageNationService pageNationService;

    final Message message;

    public EmployeeApiController(EmployeeService employeeManagementService, DepartmentService departmentService, UserManagementService userManagementService, PageNationService pageNationService, Message message) {
        this.employeeManagementService = employeeManagementService;
        this.departmentService = departmentService;
        this.userManagementService = userManagementService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "社員一覧を取得する", description = "社員一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Employee> pageInfo = employeeManagementService.selectAllWithPageInfo();
            PageInfo<EmployeeResource> result = pageNationService.getPageInfo(pageInfo, EmployeeResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "社員を取得する", description = "社員を取得する")
    @GetMapping("/{employeeCode}")
    public ResponseEntity<?> find(@PathVariable String employeeCode) {
        try {
            EmployeeCode code = EmployeeCode.of(employeeCode);
            Employee employee = employeeManagementService.find(code);
            return ResponseEntity.ok(EmployeeResource.from(employee));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "社員を登録する", description = "社員を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.社員更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated EmployeeResource resource) {
        try {
            Employee employee = convertEntity(resource);
            if (employeeManagementService.find(employee.getEmpCode()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.employee.already.exist")));
            }
            employeeManagementService.register(employee);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.employee.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "社員を更新する", description = "社員を更新する")
    @PutMapping("/{employeeCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.社員更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String employeeCode, @RequestBody EmployeeResource resource) {
        try {
            resource.setEmpCode(employeeCode);
            Employee employee = convertEntity(resource);
            employeeManagementService.save(employee);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.employee.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "社員を削除する", description = "社員を削除する")
    @DeleteMapping("/{employeeCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.社員削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String employeeCode) {
        try {
            EmployeeCode code = EmployeeCode.of(employeeCode);
            Employee employee = employeeManagementService.find(code);
            if (employee == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.employee.not.exist")));
            }
            employeeManagementService.delete(code);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.employee.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "社員を検索する", description = "社員を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody EmployeeCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            EmployeeCriteria criteria = convertToCriteria(resource);
            PageInfo<Employee> entity = employeeManagementService.searchWithPageInfo(criteria);
            PageInfo<EmployeeResource> result = pageNationService.getPageInfo(entity, EmployeeResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private Employee convertEntity(EmployeeResource resource) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        resource.setDepartmentStartDate(Optional.ofNullable(resource.getDepartmentStartDate())
                .map(date -> LocalDateTime.parse(date, formatter))
                .map(date -> date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .orElse(null));

        Department department = Optional.ofNullable(resource.getDepartmentCode())
                .flatMap(code -> Optional.ofNullable(resource.getDepartmentStartDate())
                        .map(date -> departmentService.find(DepartmentId.of(code, LocalDateTime.parse(date)))))
                .orElse(null);
        if (department == null) {
            throw new IllegalArgumentException("部門が存在しません。");
        }

        User user = null;
        if (resource.getUserId() != null && !resource.getUserId().isEmpty()) {
            user = Optional.of(resource.getUserId())
                    .map(id -> userManagementService.find(UserId.of(id)))
                    .orElseThrow(() -> new IllegalArgumentException("ユーザが存在しません。"));
        }

        Employee employee = Employee.of(
                resource.getEmpCode(),
                resource.getEmpFirstName() + " " + resource.getEmpLastName(),
                resource.getEmpFirstName() + " " + resource.getEmpLastName(),
                resource.getTel(),
                resource.getFax(),
                resource.getOccuCode());

        return Employee.of(employee, department, user);
    }
}
