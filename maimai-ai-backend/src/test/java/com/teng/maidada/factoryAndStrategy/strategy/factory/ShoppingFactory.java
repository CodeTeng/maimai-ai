package com.teng.maidada.factoryAndStrategy.strategy.factory;

import com.teng.maidada.factoryAndStrategy.enums.DiscountEnum;
import com.teng.maidada.factoryAndStrategy.strategy.context.ShoppingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 商品工厂
 * @author: ~Teng~
 * @date: 2024/6/26 20:47
 */
@Component
@Slf4j
public class ShoppingFactory {
    @Resource
    private List<ShoppingStrategy> shoppingStrategyList;

    private final Map<DiscountEnum, ShoppingStrategy> shoppingStrategyMap = new HashMap<>();

    /**
     * 根据code获取折扣策略
     *
     * @param code code
     * @return DiscountEnum
     */
    public ShoppingStrategy getStrategy(int code) {
        DiscountEnum discountEnum = DiscountEnum.valueOf(code);
        return shoppingStrategyMap.get(discountEnum);
    }

    /**
     * 注册策略枚举
     */
    @PostConstruct
    public void register() {
        shoppingStrategyList.forEach(shoppingStrategy -> {
            DiscountEnum discountEnum = shoppingStrategy.getStrategy();
            shoppingStrategyMap.put(discountEnum, shoppingStrategy);
        });
    }
}
