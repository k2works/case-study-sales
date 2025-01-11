package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 取引先コード
 */

@Value
@NoArgsConstructor(force = true)
public class PartnerName {
    String name;
    String nameKana;

    public PartnerName(String name, String nameKana) {
        if (name == null) {
            throw new IllegalArgumentException("名称は必須です");
        }

        if (name.length() > 40) {
            throw new IllegalArgumentException("名称は40桁以内である必要があります:" + name);
        }

        if (nameKana == null) {
            throw new IllegalArgumentException("カナ名称は必須です");
        }

        if (nameKana.length() > 40) {
            throw new IllegalArgumentException("カナ名称は40桁以内である必要があります:" + nameKana);
        }

        this.name = name;
        this.nameKana = nameKana;
    }

    public static PartnerName of(String name, String nameKana) {
        return new PartnerName(name, nameKana);
    }
}
