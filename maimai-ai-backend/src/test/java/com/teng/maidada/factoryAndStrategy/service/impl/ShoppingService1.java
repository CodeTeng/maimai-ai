package com.teng.maidada.factoryAndStrategy.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.teng.maidada.factoryAndStrategy.cache.ShoppingGoodsCache;
import com.teng.maidada.factoryAndStrategy.cache.ShoppingUserCache;
import com.teng.maidada.factoryAndStrategy.enums.DiscountEnum;
import com.teng.maidada.factoryAndStrategy.model.ShoppingGoods;
import com.teng.maidada.factoryAndStrategy.model.ShoppingUser;
import com.teng.maidada.factoryAndStrategy.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map;

/**
 * @description: 普通实现
 * @author: ~Teng~
 * @date: 2024/6/26 18:27
 */
@Service("shoppingService1")
@Slf4j
public class ShoppingService1 implements ShoppingService {
    @Resource
    private ShoppingUserCache userCache;

    @Resource
    private ShoppingGoodsCache goodsCache;

    /**
     * 购买商品
     *
     * @param shoppingUserId        用户ID
     * @param shoppingGoodsId       商品ID
     * @return 购买结果
     */
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
        log.info("current user: {}", JSON.toJSONString(shoppingUser));
        log.info("current merchandise: {}", JSON.toJSONString(shoppingGoods));
        DecimalFormat df = new DecimalFormat("0.00");
        DiscountEnum discountEnum = DiscountEnum.valueOf(shoppingUser.getType());
        log.info("user type: {}, 享受折扣：{}", discountEnum.getType(), df.format(discountEnum.getDiscount()));
        // 执行购买逻辑
        if (shoppingUser.getType().equals(0)) {
            // 普通用户
            return MapUtil.ofEntries(
                    MapUtil.entry("用户姓名", shoppingUser.getName()),
                    MapUtil.entry("用户性别", shoppingUser.getGender()),
                    MapUtil.entry("用户年龄", shoppingUser.getAge()),
                    MapUtil.entry("用户等级", discountEnum.getType()),
                    MapUtil.entry("商品名称", shoppingGoods.getName()),
                    MapUtil.entry("商品价格", shoppingGoods.getPrice()),
                    MapUtil.entry("折扣价格", shoppingGoods.getPrice().multiply(discountEnum.getDiscount())));
        } else {
            if (shoppingUser.getType().equals(1)) {
                // 普通 VIP
                return MapUtil.ofEntries(
                        MapUtil.entry("用户姓名", shoppingUser.getName()),
                        MapUtil.entry("用户性别", shoppingUser.getGender()),
                        MapUtil.entry("用户年龄", shoppingUser.getAge()),
                        MapUtil.entry("用户等级", discountEnum.getType()),
                        MapUtil.entry("商品名称", shoppingGoods.getName()),
                        MapUtil.entry("商品价格", shoppingGoods.getPrice()),
                        MapUtil.entry("应享折扣", discountEnum.getDiscount()),
                        MapUtil.entry("折扣价格", shoppingGoods.getPrice().multiply(discountEnum.getDiscount())));
            } else if (shoppingUser.getType().equals(2)) {
                // 高级 VIP
                return MapUtil.ofEntries(
                        MapUtil.entry("用户姓名", shoppingUser.getName()),
                        MapUtil.entry("用户性别", shoppingUser.getGender()),
                        MapUtil.entry("用户年龄", shoppingUser.getAge()),
                        MapUtil.entry("用户等级", discountEnum.getType()),
                        MapUtil.entry("商品名称", shoppingGoods.getName()),
                        MapUtil.entry("商品价格", shoppingGoods.getPrice()),
                        MapUtil.entry("应享折扣", discountEnum.getDiscount()),
                        MapUtil.entry("折扣价格", shoppingGoods.getPrice().multiply(discountEnum.getDiscount())));
            } else if (shoppingUser.getType().equals(3)) {
                // 至尊 VIP
                return MapUtil.ofEntries(
                        MapUtil.entry("用户姓名", shoppingUser.getName()),
                        MapUtil.entry("用户性别", shoppingUser.getGender()),
                        MapUtil.entry("用户年龄", shoppingUser.getAge()),
                        MapUtil.entry("用户等级", discountEnum.getType()),
                        MapUtil.entry("商品名称", shoppingGoods.getName()),
                        MapUtil.entry("商品价格", shoppingGoods.getPrice()),
                        MapUtil.entry("应享折扣", discountEnum.getDiscount()),
                        MapUtil.entry("折扣价格", shoppingGoods.getPrice().multiply(discountEnum.getDiscount())));
            } else if (shoppingUser.getType().equals(4)) {
                // 超级 VIP
                return MapUtil.ofEntries(
                        MapUtil.entry("用户姓名", shoppingUser.getName()),
                        MapUtil.entry("用户性别", shoppingUser.getGender()),
                        MapUtil.entry("用户年龄", shoppingUser.getAge()),
                        MapUtil.entry("用户等级", discountEnum.getType()),
                        MapUtil.entry("商品名称", shoppingGoods.getName()),
                        MapUtil.entry("商品价格", shoppingGoods.getPrice()),
                        MapUtil.entry("应享折扣", discountEnum.getDiscount()),
                        MapUtil.entry("折扣价格", shoppingGoods.getPrice().multiply(discountEnum.getDiscount())));
            }
        }
        return Collections.emptyMap();
    }
}
