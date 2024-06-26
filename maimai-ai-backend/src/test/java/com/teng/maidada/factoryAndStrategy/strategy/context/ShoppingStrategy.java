package com.teng.maidada.factoryAndStrategy.strategy.context;

import com.teng.maidada.factoryAndStrategy.enums.DiscountEnum;
import com.teng.maidada.factoryAndStrategy.model.ShoppingGoods;
import com.teng.maidada.factoryAndStrategy.model.ShoppingUser;

import java.util.Map;

/**
 * @description: 购买策略接口
 * @author: ~Teng~
 * @date: 2024/6/26 18:47
 */
public interface ShoppingStrategy {
    /**
     * 购买
     *
     * @param shoppingUser        用户
     * @param shoppingGoods       商品
     * @return 结果
     */
    Map<String, Object> buy(ShoppingUser shoppingUser, ShoppingGoods shoppingGoods);

    /**
     * 返回应享折扣枚举
     *
     * @return 折扣
     */
    DiscountEnum getStrategy();
}
