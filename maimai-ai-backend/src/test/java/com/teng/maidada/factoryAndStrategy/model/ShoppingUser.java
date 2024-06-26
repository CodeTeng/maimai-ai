package com.teng.maidada.factoryAndStrategy.model;

import lombok.Builder;
import lombok.Data;

/**
 * @description: 用户实体类
 * @author: ~Teng~
 * @date: 2024/6/26 18:10
 */
@Data
@Builder
public class ShoppingUser {
    /**
     * ID
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别
     */
    private String gender;
    /**
     * 用户类型 0 普通用户 1 普通VIP 2 高级VIP 3 至尊VIP 4 超级VIP
     */
    private Integer type;
}
