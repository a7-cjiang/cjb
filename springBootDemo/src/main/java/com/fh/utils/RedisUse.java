package com.fh.utils;

import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisUse {


    public static void set(String key,String value){
        Jedis jedis = RedisPoolUtil.getJedisPool();
        jedis.set(key,value);
        RedisPoolUtil.backJedis(jedis);
    }

    public static void set(String key,String value,Integer seconds){
        Jedis jedis = RedisPoolUtil.getJedisPool();
        jedis.setex(key,seconds,value);
        RedisPoolUtil.backJedis(jedis);
    }

    public static String get(String key){
        Jedis jedis = RedisPoolUtil.getJedisPool();
        String value=jedis.get(key);
        RedisPoolUtil.backJedis(jedis);
        return value;
    }

    public static void hset(String key,String field,String value){
        Jedis jedisPool = RedisPoolUtil.getJedisPool();
        jedisPool.hset(key,field,value);
        RedisPoolUtil.backJedis(jedisPool);
    }

    public static Long hlen(String key){
        Jedis jedisPool = RedisPoolUtil.getJedisPool();
        Long hlen = jedisPool.hlen(key);
        RedisPoolUtil.backJedis(jedisPool);
        return hlen;
    }

    public static String hget(String key,String field){
        Jedis jedisPool = RedisPoolUtil.getJedisPool();
        String hget = jedisPool.hget(key, field);
        RedisPoolUtil.backJedis(jedisPool);
        return hget;
    }

    public static List<String> hvals(String key){
        Jedis jedisPool = RedisPoolUtil.getJedisPool();
        List<String> hvals = jedisPool.hvals(key);
        RedisPoolUtil.backJedis(jedisPool);
        return hvals;
    }

    public static void hdel(String key,String field){
        Jedis jedisPool = RedisPoolUtil.getJedisPool();
        jedisPool.hdel(key,field);
        RedisPoolUtil.backJedis(jedisPool);
    }

    public static boolean exists(String key){
        Jedis jedisPool = RedisPoolUtil.getJedisPool();
        Boolean exists = jedisPool.exists(key);
        RedisPoolUtil.backJedis(jedisPool);
        return exists;
    }


}
