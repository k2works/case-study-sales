package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.master.department.DepartmentService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
            DepartmentList departments = departmentManagementService.selectAll();
            return ResponseEntity.ok(new PageInfo<>(departments.asList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を取得する", description = "部門を取得する")
    @GetMapping("/{departmentCode}/{departmentStartDate}")
    public ResponseEntity<?> find(@PathVariable String departmentCode, @PathVariable String departmentStartDate) {
        try {
            DepartmentId departmentId = DepartmentId.of(departmentCode, LocalDateTime.parse(departmentStartDate));
            Department department = departmentManagementService.find(departmentId);
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を登録する", description = "部門を登録する")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated DepartmentResource resource) {
        try {
            Department department = Department.of(
                    DepartmentId.of(resource.getDepartmentCode(), LocalDateTime.parse(resource.getStartDate())),
                    LocalDateTime.parse(resource.getEndDate()),
                    resource.getDepartmentName(),
                    Integer.parseInt(resource.getLayer()),
                    resource.getPath(),
                    Integer.parseInt(resource.getLowerType()),
                    Integer.parseInt(resource.getSlitYn())
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
    public ResponseEntity<?> update(@PathVariable String departmentCode, @PathVariable String departmentStartDate, @RequestBody DepartmentResource departmentResource) {
        try {
            DepartmentId departmentId = DepartmentId.of(departmentCode, LocalDateTime.parse(departmentStartDate));
            Department department = Department.of(
                    departmentId,
                    LocalDateTime.parse(departmentResource.getEndDate()),
                    departmentResource.getDepartmentName(),
                    Integer.parseInt(departmentResource.getLayer()),
                    departmentResource.getPath(),
                    Integer.parseInt(departmentResource.getLowerType()),
                    Integer.parseInt(departmentResource.getSlitYn())
            );
            departmentManagementService.save(department);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.department.updated")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部門を削除する", description = "部門を削除する")
    @DeleteMapping("/{departmentCode}/{departmentStartDate}")
    public ResponseEntity<?> delete(@PathVariable String departmentCode, @PathVariable String departmentStartDate) {
        try {
            DepartmentId departmentId = DepartmentId.of(departmentCode, LocalDateTime.parse(departmentStartDate));
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
}