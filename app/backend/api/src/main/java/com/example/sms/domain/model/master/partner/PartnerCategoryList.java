package com.example.sms.domain.model.master.partner;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * 取引先分類一覧
 */
public class PartnerCategoryList {
    List<PartnerCategoryType> value;

    public PartnerCategoryList(List<PartnerCategoryType> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public PartnerCategoryList add(PartnerCategoryType partnerCategoryType) {
        List<PartnerCategoryType> newValue = value;
        newValue.add(partnerCategoryType);
        return new PartnerCategoryList(newValue);
    }

    public List<PartnerCategoryType> asList() {
        return value;
    }

    /**
     * 雑区分
     */
    @Getter
    public enum MiscellaneousType {
        対象外(0),
        対象(1);

        private final int code;

        MiscellaneousType(int code) {
            this.code = code;
        }

        public static MiscellaneousType fromCode(int code) {
            for (MiscellaneousType value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
            throw new IllegalArgumentException("雑区分に該当する値が存在しません。");
        }
    }
}
