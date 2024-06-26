package com.teng.maidada.factoryAndStrategy.service.impl;

import com.teng.maidada.factoryAndStrategy.cache.ShoppingGoodsCache;
import com.teng.maidada.factoryAndStrategy.cache.ShoppingUserCache;
import com.teng.maidada.factoryAndStrategy.model.ShoppingGoods;
import com.teng.maidada.factoryAndStrategy.model.ShoppingUser;
import com.teng.maidada.factoryAndStrategy.service.ShoppingService;
import com.teng.maidada.factoryAndStrategy.strategy.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * @description: 策略模式实现
 * @author: ~Teng~
 * @date: 2024/6/26 18:45
 */
@Service("shoppingService2")
public class ShoppingService2 implements ShoppingService {
    @Resource
    private ShoppingUserCache userCache;
    @Resource
    private ShoppingGoodsCache goodsCache;
    @Resource
    private NormalStrategy normalStrategy;
    @Resource
    private VIPStrategy vipStrategy;
    @Resource
    private PVIPStrategy pvipStrategy;
    @Resource
    private EVIPStrategy evipStrategy;
    @Resource
    private SVIPStrategy svipStrategy;

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
        if (shoppingUser.getType().equals(0)) {
            return normalStrategy.buy(shoppingUser, shoppingGoods);
        } else {
            if (shoppingUser.getType().equals(1)) {
                return vipStrategy.buy(shoppingUser, shoppingGoods);
            } else if (shoppingUser.getType().equals(2)) {
                return pvipStrategy.buy(shoppingUser, shoppingGoods);
            } else if (shoppingUser.getType().equals(3)) {
                return evipStrategy.buy(shoppingUser, shoppingGoods);
            } else if (shoppingUser.getType().equals(4)) {
                return svipStrategy.buy(shoppingUser, shoppingGoods);
            } else {
                return Collections.emptyMap();
            }
        }
    }
}
