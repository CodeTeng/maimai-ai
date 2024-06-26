package com.teng.maidada.factoryAndStrategy.cache.handler;

import com.teng.maidada.factoryAndStrategy.cache.context.CacheInitializationAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 热启动
 * @author: ~Teng~
 * @date: 2024/6/26 18:16
 */
@Slf4j
@Component
public class CacheInitializer {
    @Resource
    private List<CacheInitializationAware> cacheInitializationAwareList;

    @PostConstruct
    public void load() {
        cacheInitializationAwareList.forEach(CacheInitializationAware::init);
    }
}
