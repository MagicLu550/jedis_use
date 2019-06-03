package net.noyark.redis_demo;

import org.junit.Test;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 对于jedis客户端的测试demo
 */
public class TestJedis {


    @Test
    public void jedisConnection(){
        //host参数表示连接的ip地址
        //port表示登录的端口

        Jedis jedis = new Jedis("www.noyark.net",6380);
        jedis.set("hello","11");

        for(int i =0;i<100;i++){
            jedis.set("hello"+i,i+"");
        }
        jedis.close();
    }

    @Test
    public void cacheLogic(){
        System.out.println("用户请求访问商品");
        //到缓存获取数据/判断数据，根据商品生成自定义key
        String key="ITEM_1306272";
        Jedis jedis = new Jedis("www.noyark.net",6380);

        if(jedis.exists(key)){
            //存在
            String result = jedis.get(key);
            System.out.println("获取到"+result);
        }else{
            String item = "{'id':1306272,'title':'华夏大平板'}";
            System.out.println("从数据获取商品信息"+item);

            jedis.set(key,item);
        }
    }

    @Test
    public void TestClear(){
        Jedis jedis6380 = new Jedis("www.noyark.net",6380);
        Jedis jedis6381 = new Jedis("www.noyark.net",6381);
        Jedis jedis6382 = new Jedis("www.noyark.net",6382);

        jedis6380.flushAll();
        jedis6381.flushAll();
        jedis6382.flushAll();
    }

    /**
     * 自定义数据分片
     */

    @Test
    public void autoShard(){
        //存储过程逻辑
        Jedis[] jedis = new Jedis[3];
        jedis[0]= new Jedis("www.noyark.net",6380);
        jedis[1] = new Jedis("www.noyark.net",6381);
        jedis[2] = new Jedis("www.noyark.net",6382);

        for(int i = 0;i<100;i++){
            Integer key = i;
            String value = "value"+i;
            int index = (value.hashCode()&Integer.MAX_VALUE)%3;
            jedis[index].set(i+"",value);
        }
        for(Jedis jedis1:jedis){
            Set<String> keys = jedis1.keys("*");
            System.out.println(keys);
        }
    }

    @Test
    public void testHashShard(){
        String str = "111qqtt1";
        System.out.println(str.hashCode());
        int i = ((str.hashCode())&Integer.MAX_VALUE)%3;
        System.out.println(i);
    }
    /**
     * jedishash一致性分片，分片对象
     */
    @Test
    public void jedisHashShard(){
        //分片对象底层实现了分片的逻辑，只需要收集所有
        //连接信息，自动完成分片计算，调用方法和jedis一样
        //手机redis节点信息
        List<JedisShardInfo> infoList = new ArrayList<>();
        infoList.add(new JedisShardInfo("www.noyark.net",6380));
        infoList.add(new JedisShardInfo("www.noyark.net",6381));
        infoList.add(new JedisShardInfo("www.noyark.net",6382));

        ShardedJedis shardedJedis = new ShardedJedis(infoList);
        shardedJedis.set("LHSLODS",11+"");

    }

    /**
     * jedis分片连接池
     */
    @Test
    public void jedisPool(){
        //利用节点信息，配置对象，最大连接数，最小连接数，最小空闲数
        //连接超时,connectTime,sockeyTime
        List<JedisShardInfo> infoList = new ArrayList<>();
        infoList.add(new JedisShardInfo("www.noyark.net",6380));
        infoList.add(new JedisShardInfo("www.noyark.net",6381));
        infoList.add(new JedisShardInfo("www.noyark.net",6382));

        ShardedJedis shardedJedis = new ShardedJedis(infoList);

        //构造一个具有配置条件的配置对象
        JedisPoolConfig config =new JedisPoolConfig();
        config.setMaxIdle(8);//最大空闲数量
        config.setMaxTotal(200);
        //从pool获取分片对象操作集群
        ShardedJedisPool pool = new ShardedJedisPool(config,infoList);
        ShardedJedis sjedis = pool.getResource();
        pool.returnResource(sjedis);
    }

    public void testNextHashShard(){

        List<JedisShardInfo> infos = new ArrayList<>();
        infos.add(new JedisShardInfo("www.noyark.net",6380));
        infos.add(new JedisShardInfo("www.noyark.net",6381));

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(8);
        config.setMaxTotal(200);

        ShardedJedisPool pool = new ShardedJedisPool(config,infos);
        ShardedJedis jedis = pool.getResource();

    }

}
