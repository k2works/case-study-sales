package com.example.sms.presentation.api.system.download;

import com.example.sms.domain.model.system.download.*;
import com.example.sms.domain.type.download.DownloadTarget;
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
    private String fileName;

    public static DownloadCondition of(DownloadTarget target) {
        return switch (target) {
            case 部門 -> Department.of();
            case 社員 -> Employee.of();
            case 商品分類 -> ProductCategory.of();
            case 商品 -> Product.of();
        };
    }
}
