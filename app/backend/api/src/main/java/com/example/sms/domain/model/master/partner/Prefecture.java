package com.example.sms.domain.model.master.partner;

import lombok.Getter;

/**
 * 都道府県
 */
@Getter
public enum Prefecture {
    // 都道府県一覧 (都道府県コード順)
    北海道(1, "北海道"),
    青森県(2, "東北地方"),
    岩手県(3, "東北地方"),
    宮城県(4, "東北地方"),
    秋田県(5, "東北地方"),
    山形県(6, "東北地方"),
    福島県(7, "東北地方"),
    茨城県(8, "関東地方"),
    栃木県(9, "関東地方"),
    群馬県(10, "関東地方"),
    埼玉県(11, "関東地方"),
    千葉県(12, "関東地方"),
    東京都(13, "関東地方"),
    神奈川県(14, "関東地方"),
    新潟県(15, "中部地方"),
    富山県(16, "中部地方"),
    石川県(17, "中部地方"),
    福井県(18, "中部地方"),
    山梨県(19, "中部地方"),
    長野県(20, "中部地方"),
    岐阜県(21, "中部地方"),
    静岡県(22, "中部地方"),
    愛知県(23, "中部地方"),
    三重県(24, "近畿地方"),
    滋賀県(25, "近畿地方"),
    京都府(26, "近畿地方"),
    大阪府(27, "近畿地方"),
    兵庫県(28, "近畿地方"),
    奈良県(29, "近畿地方"),
    和歌山県(30, "近畿地方"),
    鳥取県(31, "中国地方"),
    島根県(32, "中国地方"),
    岡山県(33, "中国地方"),
    広島県(34, "中国地方"),
    山口県(35, "中国地方"),
    徳島県(36, "四国地方"),
    香川県(37, "四国地方"),
    愛媛県(38, "四国地方"),
    高知県(39, "四国地方"),
    福岡県(40, "九州地方"),
    佐賀県(41, "九州地方"),
    長崎県(42, "九州地方"),
    熊本県(43, "九州地方"),
    大分県(44, "九州地方"),
    宮崎県(45, "九州地方"),
    鹿児島県(46, "九州地方"),
    沖縄県(47, "九州地方");

    /**
     * -- GETTER --
     *  都道府県コードを取得
     */
    private final int code;         // 都道府県コード
    /**
     * -- GETTER --
     *  地域名を取得
     */
    private final String region;    // 地域名

    Prefecture(int code, String region) {
        this.code = code;
        this.region = region;
    }

    /**
     * コードから都道府県を取得 (例: 13 -> 東京都)
     */
    public static Prefecture fromCode(int code) {
        for (Prefecture pref : values()) {
            if (pref.code == code) {
                return pref;
            }
        }
        throw new IllegalArgumentException("無効なコード: " + code);
    }

    /**
     * 都道府県名からEnumオブジェクトを取得
     */
    public static Prefecture fromName(String name) {
        for (Prefecture pref : values()) {
            if (pref.name().equals(name)) {
                return pref;
            }
        }
        throw new IllegalArgumentException("無効な都道府県名: " + name);
    }
}