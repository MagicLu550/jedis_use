package net.noyark.redis_demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringBoot整合jedis
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.nodes}")
    private String nodes;
    @Value("${spring.redis.pool.max-idle}")
    private Integer maxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private Integer minIdle;
    @Value("${spring.redis.pool.max-total}")
    private Integer maxTotal;
    @Value("${spring.redis.pool.max-wait}")
    private Integer maxWait;

    @Bean
    public JedisPoolConfig getConfig(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWaitMillis(maxWait);
        return poolConfig;
    }
    //需要使用@Bean让框架管理，注入到该用到的位置使用
    @Bean
    public ShardedJedisPool getPool(){
        //整理节点信息
        List<JedisShardInfo> infoList = new ArrayList<>();
        String[] hosts = nodes.split(",");
        for(String hostAndPoint:hosts){
            String[] info = hostAndPoint.split(":");
            System.out.println(new JedisShardInfo(info[0],Integer.parseInt(info[1])));
            infoList.add(new JedisShardInfo(info[0],Integer.parseInt(info[1])));
        }
        ShardedJedisPool pool = new ShardedJedisPool(getConfig(),infoList);
        return pool;
    }
}
