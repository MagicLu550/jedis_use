package net.noyark.redis_demo.test;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class ClusterTest {
    @Test
    public void clusterConnection(){
        //收集节点信息。整个集群提供至少一个节点
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("www.noyark.net",8000));
        nodes.add(new HostAndPort("www.noyark.net",8001));
        //构造对象之前，首先要创建个配置对象
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(8);
        config.setMaxTotal(200);
        //jedis操作分布式集群使用的分片对象
        //不需要分片计算，JedisCluster对象；构造原理在整合之后
        JedisCluster cluster = new JedisCluster(nodes,1000,config);
        for(int i = 0;i<100;i++){
            String key = "hello_key"+i;
            String value = "hello_val"+i;
            cluster.set(key,value);
            System.out.println(cluster.get(key));
        }
        //jedisCluster对象实现set,get等操作时候，有没有连接池
    }
}
