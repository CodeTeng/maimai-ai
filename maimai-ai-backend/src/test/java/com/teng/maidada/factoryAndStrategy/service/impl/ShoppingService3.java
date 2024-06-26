package com.teng.maidada.factoryAndStrategy.service.impl;

import com.teng.maidada.factoryAndStrategy.cache.ShoppingGoodsCache;
import com.teng.maidada.factoryAndStrategy.cache.ShoppingUserCache;
import com.teng.maidada.factoryAndStrategy.model.ShoppingGoods;
import com.teng.maidada.factoryAndStrategy.model.ShoppingUser;
import com.teng.maidada.factoryAndStrategy.service.ShoppingService;
import com.teng.maidada.factoryAndStrategy.strategy.context.ShoppingStrategy;
import com.teng.maidada.factoryAndStrategy.strategy.factory.ShoppingFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * @description: 工厂 + 策略模式实现
 * @author: ~Teng~
 * @date: 2024/6/26 18:46
 */
@Service("shoppingService3")
public class ShoppingService3 implements ShoppingService {
    @Resource
    private ShoppingFactory shoppingFactory;

    @Resource
    private ShoppingUserCache userCache;

    @Resource
    private ShoppingGoodsCache goodsCache;

    @Override
    public Map<String, Object> buy(Long shoppingUserId, Long shoppingGoodsId) {
        ShoppingUser shoppingUser = userCache.get(shoppingUserId);
        if (shoppingUser == null) {
            return Collections.singletonMap("error", "用户不存在");
        }
        ShoppingGoods shoppingGoods = goodsCache.get(shoppingGoodsId);
        if (shoppingGoods == null) {
            return Collections.singletonMap("error", "商品不存在");
        }
        ShoppingStrategy strategy = shoppingFactory.getStrategy(shoppingUser.getType());
        return strategy.buy(shoppingUser, shoppingGoods);
    }
}
