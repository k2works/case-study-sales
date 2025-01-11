package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 郵便番号
 */
@Value
@NoArgsConstructor(force = true)
public class PostalCode {
    String value;
    String regionCode;

    public PostalCode(String value) {
        if (value == null) {
            throw new IllegalArgumentException("郵便番号は必須です");
        }

        if (value.matches("\\d{3}-\\d{4}$")) {
            value = value.replace("-", "");
        }

        if (value.length() > 7) {
            throw new IllegalArgumentException("郵便番号は7桁以内である必要があります:" + value);
        }

        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("郵便番号は数字のみです:" + value);
        }

        this.regionCode = value.substring(0, 2);
        this.value = value;
    }

    public static PostalCode of(String postalCode) {
        return new PostalCode(postalCode);
    }

    private static final Map<String, String> postalRegionMap = new HashMap<>();

    static {
        // 北海道地方
        postalRegionMap.put("00", "北海道");
        postalRegionMap.put("04", "北海道");
        postalRegionMap.put("05", "北海道");
        postalRegionMap.put("06", "北海道");
        postalRegionMap.put("07", "北海道");
        postalRegionMap.put("08", "北海道");
        postalRegionMap.put("09", "北海道");
        // 東北地方
        postalRegionMap.put("03", "青森県");
        postalRegionMap.put("02", "岩手県");
        postalRegionMap.put("98", "宮城県");
        postalRegionMap.put("01", "秋田県");
        postalRegionMap.put("99", "山形県");
        postalRegionMap.put("96", "福島県");
        postalRegionMap.put("97", "福島県");
        // 関東地方
        postalRegionMap.put("30", "茨城県");
        postalRegionMap.put("31", "茨城県");
        postalRegionMap.put("32", "栃木県");
        postalRegionMap.put("37", "群馬県");
        postalRegionMap.put("33", "埼玉県");
        postalRegionMap.put("34", "埼玉県");
        postalRegionMap.put("35", "埼玉県");
        postalRegionMap.put("36", "埼玉県");
        postalRegionMap.put("26", "千葉県");
        postalRegionMap.put("27", "千葉県");
        postalRegionMap.put("28", "千葉県");
        postalRegionMap.put("29", "千葉県");
        postalRegionMap.put("10", "東京都");
        postalRegionMap.put("11", "東京都");
        postalRegionMap.put("12", "東京都");
        postalRegionMap.put("13", "東京都");
        postalRegionMap.put("14", "東京都");
        postalRegionMap.put("15", "東京都");
        postalRegionMap.put("16", "東京都");
        postalRegionMap.put("17", "東京都");
        postalRegionMap.put("18", "東京都");
        postalRegionMap.put("19", "東京都");
        postalRegionMap.put("20", "東京都");
        postalRegionMap.put("21", "神奈川県");
        postalRegionMap.put("22", "神奈川県");
        postalRegionMap.put("23", "神奈川県");
        postalRegionMap.put("24", "神奈川県");
        postalRegionMap.put("25", "神奈川県");
        // 中部地方
        postalRegionMap.put("94", "新潟県");
        postalRegionMap.put("95", "新潟県");
        postalRegionMap.put("93", "富山県");
        postalRegionMap.put("92", "石川県");
        postalRegionMap.put("91", "福井県");
        postalRegionMap.put("40", "山梨県");
        postalRegionMap.put("38", "長野県");
        postalRegionMap.put("39", "長野県");
        postalRegionMap.put("50", "岐阜県");
        postalRegionMap.put("41", "静岡県");
        postalRegionMap.put("42", "静岡県");
        postalRegionMap.put("43", "静岡県");
        postalRegionMap.put("44", "愛知県");
        postalRegionMap.put("45", "愛知県");
        postalRegionMap.put("46", "愛知県");
        postalRegionMap.put("47", "愛知県");
        postalRegionMap.put("48", "愛知県");
        postalRegionMap.put("49", "愛知県");
        // 近畿地方
        postalRegionMap.put("51", "三重県");
        postalRegionMap.put("52", "滋賀県");
        postalRegionMap.put("60", "京都府");
        postalRegionMap.put("61", "京都府");
        postalRegionMap.put("62", "京都府");
        postalRegionMap.put("53", "大阪府");
        postalRegionMap.put("54", "大阪府");
        postalRegionMap.put("55", "大阪府");
        postalRegionMap.put("56", "大阪府");
        postalRegionMap.put("57", "大阪府");
        postalRegionMap.put("58", "大阪府");
        postalRegionMap.put("59", "大阪府");
        postalRegionMap.put("65", "兵庫県");
        postalRegionMap.put("66", "兵庫県");
        postalRegionMap.put("67", "兵庫県");
        postalRegionMap.put("63", "奈良県");
        postalRegionMap.put("64", "和歌山県");
        // 中国地方
        postalRegionMap.put("68", "鳥取県");
        postalRegionMap.put("69", "島根県");
        postalRegionMap.put("70", "岡山県");
        postalRegionMap.put("71", "岡山県");
        postalRegionMap.put("72", "広島県");
        postalRegionMap.put("73", "広島県");
        postalRegionMap.put("74", "山口県");
        postalRegionMap.put("75", "山口県");
        // 四国地方
        postalRegionMap.put("77", "徳島県");
        postalRegionMap.put("76", "香川県");
        postalRegionMap.put("79", "愛媛県");
        postalRegionMap.put("78", "高知県");
        // 九州地方
        postalRegionMap.put("80", "福岡県");
        postalRegionMap.put("81", "福岡県");
        postalRegionMap.put("82", "福岡県");
        postalRegionMap.put("83", "福岡県");
        postalRegionMap.put("84", "佐賀県");
        postalRegionMap.put("85", "長崎県");
        postalRegionMap.put("86", "熊本県");
        postalRegionMap.put("87", "大分県");
        postalRegionMap.put("88", "宮崎県");
        postalRegionMap.put("89", "鹿児島県");
        // 沖縄地方
        postalRegionMap.put("90", "沖縄県");
    }

    public static String getRegionName(String code) {
        return postalRegionMap.getOrDefault(code, "未知の地域");
    }
}
