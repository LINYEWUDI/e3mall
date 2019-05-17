import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class SpringActiveMQConsumer {

    @Test
    public void testQueueConsumer() throws IOException {

        //初始化spring容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activeMQ.xml");

        System.in.read();

    }
}
