package net.noyark.redis_demo.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ClusterConfig {

    @Value("${spring.redis.cluster.nodes}")
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
    public JedisCluster getCluster(){
        //整理节点信息
        Set<HostAndPort> infoList = new HashSet<>();
        String[] hosts = nodes.split(",");
        for(String hostAndPoint:hosts){
            String[] info = hostAndPoint.split(":");
            infoList.add(new HostAndPort(info[0],Integer.parseInt(info[1])));
        }
        return new JedisCluster(infoList,1000,getConfig());
    }
    @Bean
    public GenericObjectPoolConfig getConfig(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        return config;
    }


}
