package com.atguigu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class RedisUtil_Sentinel {
    public static void main(String[] args) {
//        Jedis jedis = RedisUtil_Sentinel.getJedis();
        Jedis jedis = RedisUtil_Sentinel.getJedisFromSentinel();


        jedis.set("k100","v100");
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

    private static JedisSentinelPool jedisSentinelPool = null;
    public static Jedis getJedisFromSentinel(){
        if(jedisSentinelPool == null){

            Set<String> sentinels = new HashSet<>();
            sentinels.add("192.168.1.102:26379");

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

            // 创建哨兵池
            jedisSentinelPool = new JedisSentinelPool("mymaster",sentinels,jedisPoolConfig);

        }
        Jedis jedis = jedisSentinelPool.getResource();
        return jedis;
    }









}
