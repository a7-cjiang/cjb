package com.fh.utils;

import redis.clients.jedis.Jedis;

public class TestRedis {



//    public static void main(String[] args){
//        Jedis jedis = new Jedis("192.168.152.129",7020);
//
//        System.out.println(jedis.ping());
//        jedis.close();
//    }

    public static void main(String[] args){
        Jedis jedis = RedisPoolUtil.getJedisPool();
//        jedis.set("name","zhangsan");
//        String name = jedis.get("name");
//        jedis.del("type");
        System.out.println(jedis.get("type"));
        RedisPoolUtil.backJedis(jedis);
    }

}
