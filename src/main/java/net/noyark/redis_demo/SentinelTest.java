package net.noyark.redis_demo;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * 对于哨兵集群的jedis配置
 */

public class SentinelTest {

    public void sentinel(){
        //多个哨兵之间没有监控关系，顺序遍历，哪个能通就走哪个,不通，再检查
        Set<String> sentinels = new HashSet<>();
        sentinels.add(new HostAndPort("www.noyark.net",26379).toString());
        sentinels.add(new HostAndPort("www.noyark.net",26380).toString());
        sentinels.add(new HostAndPort("www.noyark.net",26381).toString());

        //mymaster是在sentinel.conf中配置的名称
        //sentinel monitor mymaster 192.168.163.200 6380
        JedisSentinelPool pool = new JedisSentinelPool("mymaster",sentinels);
        System.out.println("当前master:"+pool.getCurrentHostMaster());
        Jedis jedis =  pool.getResource();
        //jedis.auth("123456")
        jedis.set("num","1000");
        System.out.println(jedis.get("num"));
        pool.returnResource(jedis);

        pool.destroy();
        System.out.println("ok");
    }

}
