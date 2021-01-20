package com.atguigu.redis;

import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

public class RedisUtil_Cluster {
    public static void main(String[] args) {

        JedisCluster jedisCluster = getJedisCluster();
        jedisCluster.set("k100","v100");
        System.out.println(jedisCluster.get("k100"));

        jedisCluster.close();// 把整个连接池关闭


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
    private static JedisCluster jedisCluster = null;

    public static JedisCluster getJedisCluster(){
        if(jedisCluster == null){
            Set<HostAndPort> nodes = new HashSet<>();
            nodes.add(new HostAndPort("192.168.1.102",6390));
            nodes.add(new HostAndPort("192.168.1.102",6391));

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

            jedisCluster = new JedisCluster(nodes, jedisPoolConfig);

        }
        return jedisCluster;
    }

}
