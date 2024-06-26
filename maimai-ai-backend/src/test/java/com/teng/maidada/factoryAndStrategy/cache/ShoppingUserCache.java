package com.teng.maidada.factoryAndStrategy.cache;

import com.alibaba.fastjson.JSON;
import com.teng.maidada.factoryAndStrategy.cache.context.AbstractCache;
import com.teng.maidada.factoryAndStrategy.model.ShoppingUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 用户缓存
 * @author: ~Teng~
 * @date: 2024/6/26 18:17
 */
@Slf4j
@Component
public class ShoppingUserCache extends AbstractCache<Long, ShoppingUser> {
    private final Map<Long, ShoppingUser> shoppingUserMap = new HashMap<>();

    /**
     * 获取缓存
     * @param key 键
     * @return 用户实体
     */
    @Override
    public ShoppingUser get(Long key) {
        return shoppingUserMap.get(key);
    }

    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        shoppingUserMap.clear();
    }

    /**
     * 初始化缓存
     */
    @Override
    public void init() {
        this.shoppingUserMap.put(1L, ShoppingUser.builder().id(1L).name("Lucy").gender("女").age(18).type(0).build());
        this.shoppingUserMap.put(2L, ShoppingUser.builder().id(2L).name("Jack").gender("男").age(20).type(1).build());
        this.shoppingUserMap.put(3L, ShoppingUser.builder().id(3L).name("Mick").gender("男").age(22).type(2).build());
        this.shoppingUserMap.put(4L, ShoppingUser.builder().id(4L).name("Andy").gender("女").age(23).type(3).build());
        this.shoppingUserMap.put(5L, ShoppingUser.builder().id(5L).name("Pelin").gender("女").age(23).type(4).build());
        log.info("user cache init success: {}", JSON.toJSONString(this.shoppingUserMap));
    }
}
