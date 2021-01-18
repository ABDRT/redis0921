package com.atguigu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;
// 1 检查地址端口
// 2 检查bind是否注掉了
// 3 检查连接池资源是否耗尽，jedis使用后 没有通过close还给池子
public class RedisUtil {
    public static void main(String[] args) {
        Jedis jedis = RedisUtil.getJedis();

        Set<String> keys = jedis.keys("*");

        for (String key : keys) {
            System.out.println(key);
        }


        System.out.println(jedis.ping());
        jedis.close();


    }

    private static JedisPool jedisPool = null;

    public static Jedis getJedis(){
        if(jedisPool == null){
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(100);
            jedisPoolConfig.setMinIdle(20);
            jedisPoolConfig.setMaxIdle(30);
            // 资源耗尽时等待
            jedisPoolConfig.setBlockWhenExhausted(true);
            jedisPoolConfig.setMaxWaitMillis(5000);
            // 从池中取连接后要进行测试
            // 导致连接池中的连接坏掉：
            // 1 服务器端重启过 2 网断过 3 服务器端维持空闲连接超时
            jedisPoolConfig.setTestOnBorrow(true);
            jedisPool = new JedisPool("hadoop102",6379);
        }
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
