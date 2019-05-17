package xyz.test.avtiveMQTest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class ActiveMQTopicConsumer {



    @Test
    public void testTopicConsumer() throws Exception {
//      第一步：创建一个ConnectionFactory对象。
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
//      第二步：从ConnectionFactory对象中获得一个Connection对象。
        Connection connection = factory.createConnection();
//      第三步：开启连接。调用Connection对象的start方法。
        connection.start();
//      第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//      第五步：使用Session对象创建一个Destination对象。和发送端保持一致topic，并且话题的名称一致。
        Topic topic = session.createTopic("test_topic");
//      第六步：使用Session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(topic);
//      第七步：接收消息。
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //强转成文本
                TextMessage textMessage = (TextMessage)message;
                //获取信息
                String text = null;
                try {
                    text = textMessage.getText();
                    //      第八步：打印消息。
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        System.in.read();

//      第九步：关闭资源
        session.close();
        consumer.close();
        connection.close();


    }
}
