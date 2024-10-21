package com.example.sms.domain.model.master.employee;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

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
        name = name.replace("　", " ");
        if (!name.contains(" ")) {
            throw new IllegalArgumentException("名前は姓と名をスペースで区切ってください。");
        }
        nameKana = nameKana.replace("　", " ");
        if (!nameKana.contains(" ")) {
            throw new IllegalArgumentException("名前カナは姓と名をスペースで区切ってください。");
        }

        String[] names = name.split(" ");
        String[] namesKana = nameKana.split(" ");
        String firstName = names[0];
        String lastName = names[1];
        String firstNameKana = namesKana[0];
        String lastNameKana = namesKana[1];
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
