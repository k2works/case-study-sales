package com.example.sms.domain.type.phone;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 電話番号
 */
@Value
@NoArgsConstructor(force = true)
public class PhoneNumber {
    String value;
    String areaCode;
    String localExchange;
    String subscriberNumber;

    public String getValue() {
        return areaCode + "-" + localExchange + "-" + subscriberNumber;
    }

    public PhoneNumber(String value, String areaCode, String localExchange, String subscriberNumber) {
        this.areaCode = areaCode;
        this.localExchange = localExchange;
        this.subscriberNumber = subscriberNumber;
        if (value == null) {
            throw new IllegalArgumentException("電話番号は必須です");
        } else {
            this.value = value;
        }
    }

    public static PhoneNumber of(String value) {
        if (value == null) {
            throw new IllegalArgumentException("電話番号は必須です");
        }
        String regex = "^(0\\d{1,4}[-\\s]?\\d{1,4}[-\\s]?\\d{4})$";
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("電話番号は適切な形式で入力してください (例: 03-9999-0000, 090 9999 0000)");
        }
        String normalized = value.replaceAll("[\\s-]", "");
        if (normalized.length() != 10 && normalized.length() != 11) {
            throw new IllegalArgumentException("電話番号は0から始まる10桁または11桁の数字で入力してください");
        }

        String areaCode;
        String localExchange;
        String subscriberNumber;
        if (normalized.length() == 11) {
            areaCode = normalized.substring(0, 3);
            localExchange = normalized.substring(3, 7);
            subscriberNumber = normalized.substring(7);
        } else {
            areaCode = normalized.substring(0, 2);
            localExchange = normalized.substring(2, 6);
            subscriberNumber = normalized.substring(6);
        }

        return new PhoneNumber(normalized, areaCode, localExchange, subscriberNumber);
    }
}
