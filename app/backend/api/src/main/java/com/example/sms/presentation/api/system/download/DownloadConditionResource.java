package com.example.sms.presentation.api.system.download;

import com.example.sms.domain.model.system.download.Department;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.domain.model.system.download.DownloadTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "ダウンロード条件")
public class DownloadConditionResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private DownloadTarget target;


    public static DownloadCondition of(DownloadTarget target) {
        return switch (target) {
            case DEPARTMENT -> Department.of();
        };
    }
}
