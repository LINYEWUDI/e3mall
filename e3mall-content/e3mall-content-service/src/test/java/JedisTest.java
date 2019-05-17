import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * 直接通过原生的Jedis操作redis
 */

public class JedisTest {

    //单机
    @Test
    public void jedisSingleText(){
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.25.128",6379);
        //进行操作
        jedis.set("hobby","program");
        String hobby = jedis.get("hooby");
        System.out.println(hobby);
        //关闭连接
        jedis.close();
    }

    //单机--连接池版
    @Test
    public void jedisPoolText(){
        //创建连接池对象
        JedisPool jedisPool = new JedisPool("192.168.25.128",6379);
        //取到jedis对象
        Jedis jedis = jedisPool.getResource();
        //进行操作
        jedis.set("student","atu");
        String student = jedis.get("student");
        System.out.println(student);

        //关闭连接
        jedis.close();
        jedisPool.close();
    }


    @Test
    public void jedisClusterText(){
        //创建节点对象Set集合
        Set<HostAndPort> set = new HashSet<>();
        set.add(new HostAndPort("192.168.25.128",7001));
        set.add(new HostAndPort("192.168.25.128",7002));
        set.add(new HostAndPort("192.168.25.128",7003));
        set.add(new HostAndPort("192.168.25.128",7004));
        set.add(new HostAndPort("192.168.25.128",7005));
        set.add(new HostAndPort("192.168.25.128",7006));

        //创建jedisCluster集群对象
        JedisCluster jedisCluster = new JedisCluster(set);
        jedisCluster.set("hobby","Java");
        String hobby = jedisCluster.get("hobby");
        System.out.println(hobby);
        jedisCluster.close();


    }
}
