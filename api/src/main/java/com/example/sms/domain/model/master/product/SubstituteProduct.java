package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 代替商品
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SubstituteProduct {
    SubstituteProductKey substituteProductKey; // 代替商品キー
    Integer priority; // 優先順位
}
