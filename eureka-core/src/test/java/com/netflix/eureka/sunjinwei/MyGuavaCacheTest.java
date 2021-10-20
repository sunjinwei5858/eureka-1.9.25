package com.netflix.eureka.sunjinwei;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * google guava cache 测试
 * 1.创建方式
 * 2.淘汰策略：基于容量；基于时间；基于引用
 * 3.底层有lru的淘汰策略 使用accessQueue writeQueue实现
 */
public class MyGuavaCacheTest {

    /**
     * 第一种方式创建
     */
    @Test
    public void test001() throws ExecutionException {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return "哈哈哈哈哈";
            }
        });
        System.out.println(cache.get("a"));
        cache.put("aaa", "ttt");
        cache.put("bbb", "ggg");
        cache.put("ccc", "nnn");
        System.out.println(cache.get("v"));
        System.out.println(cache.get("v"));
    }

    /**
     * 第二种方式创建：callable方式 初始化后 在get或者put的时候声明
     */
    @Test
    public void test002() throws ExecutionException {

        Cache<Object, Object> cache = CacheBuilder.newBuilder().build();
        cache.get("a", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "111";
            }
        });
    }

    /**
     * 淘汰策略1：基于容量maximumSize
     */
    @Test
    public void test003() throws ExecutionException {

        Cache<Object, Object> cache = CacheBuilder.newBuilder().maximumSize(2).build();
        cache.put("1", 2);
        cache.put("2", 3);
        cache.put("3", 4);
        cache.put("4", 5);

        System.out.println(cache.get("1", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 0;
            }
        }));

    }

    /**
     * 淘汰策略2：基于定时
     */
    @Test
    public void test004() {
        // expireAfterXXX 当缓存失效时 通过加锁 只有一个线程去load 其余线程会阻塞
        CacheBuilder.newBuilder().expireAfterAccess(4, TimeUnit.SECONDS).build();
        CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.SECONDS).build();

        // refreshAfterXXX 当缓存失效时 通过加锁 异步线程去load 其余线程可能会返回旧的缓存值
        CacheBuilder.newBuilder().refreshAfterWrite(4, TimeUnit.SECONDS).build();
    }

    /**
     * 淘汰策略3：基于引用 比如弱引用 软引用
     */
    @Test
    public void test005() {

        CacheBuilder.newBuilder().weakKeys().build();
        CacheBuilder.newBuilder().weakValues().build();

    }

}
