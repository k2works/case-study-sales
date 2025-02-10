package com.example.sms.domain.model.master.partner;

import com.example.sms.domain.BusinessException;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 取引先名称
 */
@Value
@NoArgsConstructor(force = true)
public class PartnerName {
    String name;
    String nameKana;

    public PartnerName(String name, String nameKana) {
        if (name == null) {
            throw new BusinessException("名称は必須です");
        }

        if (name.length() > 40) {
            throw new BusinessException("名称は40桁以内である必要があります:" + name);
        }

        if (nameKana == null) {
            throw new BusinessException("カナ名称は必須です");
        }

        if (nameKana.length() > 40) {
            throw new BusinessException("カナ名称は40桁以内である必要があります:" + nameKana);
        }

        this.name = name;
        this.nameKana = nameKana;
    }

    public static PartnerName of(String name, String nameKana) {
        return new PartnerName(name, nameKana);
    }
}
