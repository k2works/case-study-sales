package com.example.sms.domain.model.master.employee;


import com.example.sms.domain.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * 社員名
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class EmployeeName {
    String firstName; // 名
    String lastName;  // 姓
    String firstNameKana; // 名カナ
    String lastNameKana;  // 姓カナ

    public static EmployeeName of(String name, String nameKana) {
        if (name == null || name.isEmpty()) {
            name = " ";
        }
        if (nameKana == null || nameKana.isEmpty()) {
            nameKana = " ";
        }
        name = name.replace("　", " ");
        isTrue(name.contains(" "), "名前は姓と名をスペースで区切ってください。");
        nameKana = nameKana.replace("　", " ");
        isTrue(nameKana.contains(" "), "名前カナは姓と名をスペースで区切ってください。");

        String firstName = "";
        String lastName = "";
        if (!name.equals(" ")) {
            String[] names = name.split(" ");
            firstName = names[0];
            lastName = names[1];
        }

        String firstNameKana = "";
        String lastNameKana = "";
        if (!nameKana.equals(" ")) {
            String[] namesKana = nameKana.split(" ");
            firstNameKana = namesKana[0];
            lastNameKana = namesKana[1];
        }
        return new EmployeeName(firstName, lastName, firstNameKana, lastNameKana);
    }

    /**
     * 社員名
     */
    public String Name() {
        return firstName + " " + lastName;
    }

    /**
     * 社員名カナ
     */
    public String NameKana() {
        return firstNameKana + " " + lastNameKana;
    }
}
