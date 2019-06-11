package net.noyark.redis_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@SpringBootApplication
@Controller
public class RedisDemoApplication {

    @Autowired
    private ShardedJedisPool pool;

    @Autowired
    private JedisCluster cluster;

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    @RequestMapping("hello")
    public void testRedis(){
        ShardedJedis jedis = pool.getResource();
        jedis.set("1","2");//
        pool.close();
        System.out.println("设置成功");
    }
    //测试代码端高可用
    @RequestMapping
    public String clusterSetGet(String key,String value){
        cluster.set(key,value);
        return cluster.get(key);
    }


}
