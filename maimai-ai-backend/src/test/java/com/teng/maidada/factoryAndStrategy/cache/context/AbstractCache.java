package com.teng.maidada.factoryAndStrategy.cache.context;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/6/26 18:14
 */
public abstract class AbstractCache<K, V> implements CacheInitializationAware {
    /**
     * 获取
     *
     * @param key 键
     * @return 值
     */
    public abstract V get(K key);

    /**
     * 清空
     */
    public abstract void clear();

    /**
     * 重新加载
     */
    public void reload() {
        clear();
        init();
    }
}
