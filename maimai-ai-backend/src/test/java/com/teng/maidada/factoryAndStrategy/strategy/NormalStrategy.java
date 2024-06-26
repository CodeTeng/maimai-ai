package com.teng.maidada.factoryAndStrategy.strategy;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.teng.maidada.factoryAndStrategy.enums.DiscountEnum;
import com.teng.maidada.factoryAndStrategy.model.ShoppingGoods;
import com.teng.maidada.factoryAndStrategy.model.ShoppingUser;
import com.teng.maidada.factoryAndStrategy.strategy.context.ShoppingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * @description: 普通用户策略
 * @author: ~Teng~
 * @date: 2024/6/26 18:49
 */
@Slf4j
@Component
public class NormalStrategy implements ShoppingStrategy {
    @Override
    public Map<String, Object> buy(ShoppingUser shoppingUser, ShoppingGoods shoppingGoods) {
        log.info("current strategy: {}", getStrategy().getType());
        DecimalFormat df = new DecimalFormat("0.00");
        return MapUtil.ofEntries(
                MapUtil.entry("用户姓名", shoppingUser.getName()),
                MapUtil.entry("用户性别", shoppingUser.getGender()),
                MapUtil.entry("用户年龄", shoppingUser.getAge()),
                MapUtil.entry("用户等级", getStrategy().getType()),
                MapUtil.entry("商品名称", shoppingGoods.getName()),
                MapUtil.entry("商品价格", df.format(shoppingGoods.getPrice())),
                MapUtil.entry("折扣价格", df.format(shoppingGoods.getPrice().multiply(getStrategy().getDiscount()))));
    }

    @Override
    public DiscountEnum getStrategy() {
        log.info("load strategy: {}", JSON.toJSONString(DiscountEnum.NORMAL));
        return DiscountEnum.NORMAL;
    }
}
