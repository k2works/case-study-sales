package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.service.master.department.DepartmentCriteria;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DepartmentResourceDTOMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public static Department convertToEntity(DepartmentResource resource) {
        return Department.of(
                DepartmentId.of(resource.getDepartmentCode(), LocalDateTime.parse(resource.getStartDate(), FORMATTER)),
                LocalDateTime.parse(resource.getEndDate(), FORMATTER),
                resource.getDepartmentName(),
                Integer.parseInt(resource.getLayer()),
                resource.getPath(),
                resource.getLowerType().getValue(),
                resource.getSlitYn().getValue()
        );
    }

    public static DepartmentCriteria convertToCriteria(DepartmentCriteriaResource resource) {
        return DepartmentCriteria.builder()
                .departmentCode(resource.getDepartmentCode())
                .departmentName(resource.getDepartmentName())
                .startDate(resource.getStartDate() != null ? LocalDateTime.parse(resource.getStartDate(), FORMATTER) : null)
                .endDate(resource.getEndDate() != null ? LocalDateTime.parse(resource.getEndDate(), FORMATTER) : null)
                .build();
    }
}