package com.example.sms.domain.model.system.autonumber;

import lombok.Getter;

/**
 * 伝票種別コード
 */
@Getter
public enum DocumentTypeCode {
    受注("O"),
    売上("S"),
    その他("X");

    private final String code;

    DocumentTypeCode(String code) {
        this.code = code;
    }

    public static DocumentTypeCode fromCode(String code) {
        for (DocumentTypeCode documentTypeCode : DocumentTypeCode.values()) {
            if (documentTypeCode.code.equals(code)) {
                return documentTypeCode;
            }
        }
        throw new IllegalArgumentException("伝票種別コード未登録:" + code);
    }
}
