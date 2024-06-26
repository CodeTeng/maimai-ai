package com.teng.maidada.factoryAndStrategy.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 商品实体类
 * @author: ~Teng~
 * @date: 2024/6/26 18:11
 */
@Data
@Builder
public class ShoppingGoods {
    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;
}
