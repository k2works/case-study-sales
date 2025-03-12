package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.master.employee.EmployeeResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.department.DepartmentCriteria;
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

import static com.example.sms.presentation.api.master.department.DepartmentResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.master.department.DepartmentResourceDTOMapper.convertToEntity;

/**
 * 部門API
 */
@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department", description = "部門")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentApiController {
    final DepartmentService departmentManagementService;
    final PageNationService pageNationService;
    final Message message;

    public DepartmentApiController(DepartmentService departmentManagementService, PageNationService pageNationService, Message message) {
        this.departmentManagementService = departmentManagementService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "部門一覧を取得する", description = "部門一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Department> pageInfo = departmentManagementService.selectAllWithPageInfo();
            PageInfo<DepartmentResource> result = pageNationService.getPageInfo(pageInfo, DepartmentResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
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
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を登録する", description = "部門を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.部門登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated DepartmentResource resource) {
        try {
            Department department = convertToEntity(resource);
            Department departmentOptional = departmentManagementService.find(department.getDepartmentId());
            if (departmentOptional != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.department.already.exist")));
            }
            departmentManagementService.register(department);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.department.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を更新する", description = "部門を更新する")
    @PutMapping("/{departmentCode}/{departmentStartDate}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.部門更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String departmentCode, @PathVariable String departmentStartDate, @RequestBody DepartmentResource departmentResource) {
        try {
            departmentResource.setDepartmentCode(departmentCode);
            departmentResource.setStartDate(departmentStartDate);
            Department department = convertToEntity(departmentResource);
            List<Employee> addEmployees = getAddFilteredEmployees(departmentResource);
            List<Employee> deleteEmployees = getDeleteFilteredEmployees(departmentResource);

            departmentManagementService.save(department, addEmployees, deleteEmployees);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.department.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
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
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を検索する", description = "部門を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody DepartmentCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            DepartmentCriteria criteria = convertToCriteria(resource);
            PageInfo<Department> entity = departmentManagementService.searchWithPageInfo(criteria);
            PageInfo<DepartmentResource> result = pageNationService.getPageInfo(entity, DepartmentResource::from);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private static List<Employee> getAddFilteredEmployees(DepartmentResource departmentResource) {
        return departmentResource.getEmployees() == null ? Collections.emptyList() :
                departmentResource.getEmployees().stream()
                        .filter(EmployeeResource::isAddFlag)
                        .map(employeeResource -> Employee.of(
                                employeeResource.getEmpCode(),
                                employeeResource.getEmpFirstName() + " " + employeeResource.getEmpLastName(),
                                employeeResource.getEmpFirstNameKana() + " " + employeeResource.getEmpLastNameKana(),
                                employeeResource.getTel(),
                                employeeResource.getFax(),
                                employeeResource.getOccuCode()
                        ))
                        .toList();
    }

    private static List<Employee> getDeleteFilteredEmployees(DepartmentResource departmentResource) {
        return departmentResource.getEmployees() == null ? Collections.emptyList() :
                departmentResource.getEmployees().stream()
                        .filter(EmployeeResource::isDeleteFlag)
                        .map(employeeResource -> Employee.of(
                                employeeResource.getEmpCode(),
                                employeeResource.getEmpFirstName() + " " + employeeResource.getEmpLastName(),
                                employeeResource.getEmpFirstNameKana() + " " + employeeResource.getEmpLastNameKana(),
                                employeeResource.getTel(),
                                employeeResource.getFax(),
                                employeeResource.getOccuCode()
                        ))
                        .toList();
    }
}

