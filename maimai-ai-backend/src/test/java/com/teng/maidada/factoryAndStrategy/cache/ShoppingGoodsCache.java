package com.teng.maidada.factoryAndStrategy.cache;

import com.alibaba.fastjson.JSON;
import com.teng.maidada.factoryAndStrategy.cache.context.AbstractCache;
import com.teng.maidada.factoryAndStrategy.model.ShoppingGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 商品缓存
 * @author: ~Teng~
 * @date: 2024/6/26 18:21
 */
@Slf4j
@Component
public class ShoppingGoodsCache extends AbstractCache<Long, ShoppingGoods> {
    private final Map<Long, ShoppingGoods> shoppingGoodsMap = new HashMap<>();

    /**
     * 获取缓存
     * @param key 键
     * @return 商品数据
     */
    @Override
    public ShoppingGoods get(Long key) {
        return shoppingGoodsMap.get(key);
    }

    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        shoppingGoodsMap.clear();
    }

    /**
     * 初始化缓存
     */
    @Override
    public void init() {
        this.shoppingGoodsMap.put(1L, ShoppingGoods.builder().id(1L).name("小米14").price(BigDecimal.valueOf(3999.00)).build());
        this.shoppingGoodsMap.put(2L, ShoppingGoods.builder().id(2L).name("小米路由器").price(BigDecimal.valueOf(399.00)).build());
        this.shoppingGoodsMap.put(3L, ShoppingGoods.builder().id(3L).name("小米扫地机器人").price(BigDecimal.valueOf(2299.00)).build());
        this.shoppingGoodsMap.put(4L, ShoppingGoods.builder().id(4L).name("小米电视S75英寸4K").price(BigDecimal.valueOf(3899.00)).build());
        this.shoppingGoodsMap.put(5L, ShoppingGoods.builder().id(5L).name("小米SoundPro").price(BigDecimal.valueOf(899)).build());
        log.info("merchandise cache init success: {}", JSON.toJSONString(this.shoppingGoodsMap));
    }
}
