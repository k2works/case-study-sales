package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 取引先名称
 */
@Value
@NoArgsConstructor(force = true)
public class PartnerName {
    String name;
    String nameKana;

    public PartnerName(String name, String nameKana) {
        notNull(name, "名称は必須です");
        isTrue(
                name.length() <= 40,
                "名称は40桁以内である必要があります: %s",
                name
        );

        // カナ名称 (nameKana) のバリデーション
        notNull(nameKana, "カナ名称は必須です");
        isTrue(
                nameKana.length() <= 40,
                "カナ名称は40桁以内である必要があります: %s",
                nameKana
        );

        this.name = name;
        this.nameKana = nameKana;
    }

    public static PartnerName of(String name, String nameKana) {
        return new PartnerName(name, nameKana);
    }
}