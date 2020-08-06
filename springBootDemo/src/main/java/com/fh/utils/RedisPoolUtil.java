package com.fh.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//redis连接池
public class RedisPoolUtil {

    private static JedisPool jedisPool;

    private RedisPoolUtil(){}

    //staric静态块，只能加载一次，且都是静态方法
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(2);
        jedisPoolConfig.setMaxWaitMillis(30000);
        jedisPool=new JedisPool(jedisPoolConfig,"192.168.152.133",7020);
    }


    //从连接池中拿链接
    public static Jedis getJedisPool(){
        return jedisPool.getResource();
    }
    //放回连接池
    public static void backJedis(Jedis jedis){
        jedisPool.returnResource(jedis);
    }


}
