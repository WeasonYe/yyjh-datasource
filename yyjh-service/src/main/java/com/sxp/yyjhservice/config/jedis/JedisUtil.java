package com.sxp.yyjhservice.config.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class JedisUtil {
    @Autowired
    private JedisPool jedisPool;

    public void set(String key, String value,int index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            jedis.set(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //返还到连接池
            jedis.close();
        }
    }

    public String get(String key,int index)  {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.get(key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    public void del(String key,int index){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            jedis.del(key);
        }finally {
            jedis.close();
        }
    }

    public List<String> getDBS(){
        List<String> dbs = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (int i = 0;i < 16;i++){
                jedis.select(i);
                if (jedis.keys("*").size()!=0)
                    dbs.add("db"+i);
            }
        }finally {
            jedis.close();
        }
        return dbs;
    }

    public Set<String> getDBKeys(int index){
        Jedis jedis = null;
        Set<String> keys = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            keys = jedis.keys("*");
        }finally {
            jedis.close();
        }
        return keys;
    }
}
