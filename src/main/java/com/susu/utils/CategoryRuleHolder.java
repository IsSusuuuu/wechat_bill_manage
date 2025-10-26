package com.susu.utils;

import com.susu.domain.entity.CategoryRule;
import com.susu.mapper.CategoryRuleMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 分类规则缓存持有者
 * 负责缓存和提供分类规则，支持动态刷新
 *
 * @author 苏苏
 * @since 版本号
 */
@Component
public class CategoryRuleHolder {

    @Autowired
    private CategoryRuleMapper categoryRuleMapper;
    
    // 缓存所有启用的规则（按优先级升序，1优先）
    private List<CategoryRule> categoryRules;
    
    // 读写锁，保证线程安全
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    // 缓存最后更新时间
    private long lastUpdateTime = 0;
    
    // 缓存过期时间（5分钟）
    private static final long CACHE_EXPIRE_TIME = 5 * 60 * 1000;

    @PostConstruct
    public void init() {
        refreshCache();
    }
    
    /**
     * 获取规则列表
     */
    public List<CategoryRule> getRuleList() {
        lock.readLock().lock();
        try {
            // 检查缓存是否过期
            if (isCacheExpired()) {
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    // 双重检查
                    if (isCacheExpired()) {
                        refreshCache();
                    }
                } finally {
                    lock.writeLock().unlock();
                    lock.readLock().lock();
                }
            }
            return categoryRules;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 刷新缓存
     */
    public void refreshCache() {
        lock.writeLock().lock();
        try {
            categoryRules = categoryRuleMapper.selectAllEnabledRules();
            if (categoryRules != null) {
                categoryRules.sort(Comparator.comparingInt(CategoryRule::getPriority));
            }
            lastUpdateTime = System.currentTimeMillis();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 检查缓存是否过期
     */
    private boolean isCacheExpired() {
        return System.currentTimeMillis() - lastUpdateTime > CACHE_EXPIRE_TIME;
    }
    
    /**
     * 获取缓存统计信息
     */
    public String getCacheInfo() {
        lock.readLock().lock();
        try {
            return String.format("规则数量: %d, 最后更新: %s", 
                categoryRules != null ? categoryRules.size() : 0,
                new java.util.Date(lastUpdateTime).toString());
        } finally {
            lock.readLock().unlock();
        }
    }
}
