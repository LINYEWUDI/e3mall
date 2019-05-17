package xyz.test.avtiveMQTest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class ActiveMQQueueConsumerTest {

    @Test
    public void queueConsumerTest() throws JMSException, IOException {
        // 第一步：创建一个ConnectionFactory对象。
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        // 第二步：从ConnectionFactory对象中获得一个Connection对象。
        Connection connection = factory.createConnection();
        // 第三步：开启连接。调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
        Queue queue = session.createQueue("spring-queue");
        // 第六步：使用Session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(queue);
        // 第七步：接收消息。
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {//这里参数为生产者发送的信息对象
                TextMessage textMessage = (TextMessage)message;
                String text;
                try {
                    text = textMessage.getText();
                    //控制台输出
                    System.out.println(text);

                } catch (JMSException e) {
                    e.printStackTrace();
                }

            }
        });
        //等待键盘输入
        System.in.read();

        // 第九步：关闭资源
        session.close();
        consumer.close();
        connection.close();



    }
}
