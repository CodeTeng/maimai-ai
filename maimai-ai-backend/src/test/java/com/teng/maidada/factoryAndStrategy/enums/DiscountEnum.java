package com.teng.maidada.factoryAndStrategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @description: 折扣枚举
 * @author: ~Teng~
 * @date: 2024/6/26 18:12
 */
@Getter
@AllArgsConstructor
public enum DiscountEnum {
    NORMAL(0, "普通用户", BigDecimal.valueOf(1.00)),
    VIP(1, "普通VIP", BigDecimal.valueOf(0.95)),
    PVIP(2, "高级VIP", BigDecimal.valueOf(0.90)),
    EVIP(3, "至尊VIP", BigDecimal.valueOf(0.85)),
    SVIP(4, "超级VIP", BigDecimal.valueOf(0.80));

    /**
     * 用户类型code
     */
    private final int code;

    /**
     * 用户类型文字
     */
    private final String type;

    /**
     * 折扣
     */
    private final BigDecimal discount;

    public static DiscountEnum valueOf(int code) {
        for (DiscountEnum discountEnum : DiscountEnum.values()) {
            if (discountEnum.code == code) {
                return discountEnum;
            }
        }
        return null;
    }
}
