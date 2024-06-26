package com.teng.maidada.factoryAndStrategy.service;

import java.util.Map;

/**
 * @description: 购物服务
 * @author: ~Teng~
 * @date: 2024/6/26 18:24
 */
public interface ShoppingService {

    /**
     * 购买
     *
     * @param shoppingUserId        用户ID
     * @param shoppingGoodsId       商品ID
     * @return 购买结果
     */
    Map<String, Object> buy(Long shoppingUserId, Long shoppingGoodsId);
}
