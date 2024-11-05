package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 部品表
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Bom {
    BomKey bomKey; // 部品表キー
    Integer componentQuantity; // 部品数量
}
