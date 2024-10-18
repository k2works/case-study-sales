package com.example.sms.presentation.api.master.department;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "部門")
public class DepartmentResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String departmentCode;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private String departmentName;
    private String layer;
    private String path;
    private String lowerType;
    private String slitYn;
}
