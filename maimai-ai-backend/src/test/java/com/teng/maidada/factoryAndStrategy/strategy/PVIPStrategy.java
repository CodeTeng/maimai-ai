package com.teng.maidada.factoryAndStrategy.strategy;

import cn.hutool.core.map.MapUtil;
import com.teng.maidada.factoryAndStrategy.enums.DiscountEnum;
import com.teng.maidada.factoryAndStrategy.model.ShoppingGoods;
import com.teng.maidada.factoryAndStrategy.model.ShoppingUser;
import com.teng.maidada.factoryAndStrategy.strategy.context.ShoppingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 高级 VIP 策略
 * @author: ~Teng~
 * @date: 2024/6/26 20:41
 */
@Component
@Slf4j
public class PVIPStrategy implements ShoppingStrategy {
    @Override
    public Map<String, Object> buy(ShoppingUser shoppingUser, ShoppingGoods shoppingGoods) {
        log.info("高级 VIP 策略");
        return MapUtil.ofEntries(
                MapUtil.entry("用户姓名", shoppingUser.getName()),
                MapUtil.entry("用户性别", shoppingUser.getGender()),
                MapUtil.entry("用户年龄", shoppingUser.getAge()),
                MapUtil.entry("用户等级", getStrategy().getType()),
                MapUtil.entry("商品名称", shoppingGoods.getName()),
                MapUtil.entry("商品价格", shoppingGoods.getPrice()),
                MapUtil.entry("应享折扣", getStrategy().getDiscount()),
                MapUtil.entry("折扣价格", shoppingGoods.getPrice().multiply(getStrategy().getDiscount())));
    }

    @Override
    public DiscountEnum getStrategy() {
        return DiscountEnum.PVIP;
    }
}
