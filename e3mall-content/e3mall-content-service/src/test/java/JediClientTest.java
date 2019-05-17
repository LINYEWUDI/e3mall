import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xyz.e3mall.common.JedisUtils.JedisClient;

/**
 * 面向接口：初始化spring容器后，通过接口的数据类型进行自动装入，
 * 只需修改配置文件，就可达到切换单机或者集群redis的功能。
 */

public class JediClientTest {

    @Test
    public void JedisPoolTest(){
        //初始化spring容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //这一步很关键！通过class(集群和单机版的接口类型)进行装入
        JedisClient jedisClient = ac.getBean(JedisClient.class);
        String str = jedisClient.get("hobby");

        System.out.println(str);


    }

}
