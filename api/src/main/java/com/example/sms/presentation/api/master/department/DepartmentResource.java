package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.model.master.department.DepartmentLowerType;
import com.example.sms.domain.model.master.department.SlitYnType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "部門")
public class DepartmentResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String departmentCode;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;
    private String departmentName;
    private String layer;
    private String path;
    private DepartmentLowerType lowerType;
    private SlitYnType slitYn;
}
